package it.ddbdev.ticketsystem.controller;


import javax.transaction.Transactional;
import javax.validation.Valid;

import it.ddbdev.ticketsystem.mail.MailService;
import it.ddbdev.ticketsystem.utils.Constants;
import it.ddbdev.ticketsystem.entity.Authority;
import it.ddbdev.ticketsystem.entity.User;
import it.ddbdev.ticketsystem.exception.ResourceNotFoundException;
import it.ddbdev.ticketsystem.payload.request.SigninRequest;
import it.ddbdev.ticketsystem.payload.request.SignupRequest;
import it.ddbdev.ticketsystem.payload.response.JwtAuthenticationResponse;
import it.ddbdev.ticketsystem.security.JwtTokenProvider;
import it.ddbdev.ticketsystem.security.UserPrincipal;
import it.ddbdev.ticketsystem.service.AuthorityService;
import it.ddbdev.ticketsystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final AuthorityService authorityService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;


    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserService userService, AuthorityService authorityService, PasswordEncoder passwordEncoder, MailService mailService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.authorityService = authorityService;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }



    @PostMapping("signin")
    @Transactional
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SigninRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsernameOrEmail(),
                        request.getPassword()
                )
        );


        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = JwtTokenProvider.generateToken(authentication);
        JwtAuthenticationResponse currentUser = UserPrincipal.createJwtAuthenticationResponseFromUserPrincipal((UserPrincipal) authentication.getPrincipal(), jwt);
        User user = userService.getUserById(((UserPrincipal) authentication.getPrincipal()).getId()).get();
        user.setLastSignin(LocalDateTime.now());

        return ResponseEntity.ok(currentUser);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

        if(userService.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<String>("Username already in use", HttpStatus.BAD_REQUEST);
        }

        if(userService.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<String>("Email Address already in use!", HttpStatus.BAD_REQUEST);
        }

        // find default valid authority
        Authority authority = authorityService.findByDefaultAuthorityTrue()
                .orElseThrow(() -> new ResourceNotFoundException("Authority", "default active authority", true));

        // Creating user's account
        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail().toLowerCase(),
                passwordEncoder.encode(signUpRequest.getPassword()),
                Collections.singleton(authority), // transform object Authority into Set<Authority>
                UUID.randomUUID().toString().toUpperCase()
        );

        userService.save(user);

        //Invio email
        mailService.sendMail(mailService.createMail(
                user,
                Constants.MAIL_CONFIRM_SUBJECT,
                Constants.MAIL_CONFIRM_OBJECT,
                user.getConfirm()
        ));

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("confirm")
    @Transactional
    public ResponseEntity<?> confirmEmail(@RequestParam String confirmToken){
        Optional<User> foundUser = userService.getUserByConfirmToken(confirmToken);

        if (!foundUser.isPresent()){
            return new ResponseEntity<>("Token not found", HttpStatus.NOT_FOUND);
        }

        User user = foundUser.get();
        user.setEnabled(true);
        user.setConfirm(null);
        user.setAuthorities(Collections.singleton(authorityService.getByAuthorityName("ROLE_USER").get()));

        return new ResponseEntity<>("User " + user.getUsername() + " confirmed", HttpStatus.OK);
    }
}
