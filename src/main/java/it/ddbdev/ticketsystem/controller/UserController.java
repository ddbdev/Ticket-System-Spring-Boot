package it.ddbdev.ticketsystem.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.ddbdev.ticketsystem.entity.Authority;
import it.ddbdev.ticketsystem.entity.Avatar;
import it.ddbdev.ticketsystem.entity.User;
import it.ddbdev.ticketsystem.payload.request.UpdateMeRequest;
import it.ddbdev.ticketsystem.payload.request.UpdateUserAuthority;
import it.ddbdev.ticketsystem.payload.response.AuthorityUserResponse;
import it.ddbdev.ticketsystem.payload.response.UserAuthoritiesResponse;
import it.ddbdev.ticketsystem.payload.response.UserResponse;
import it.ddbdev.ticketsystem.security.CurrentUser;
import it.ddbdev.ticketsystem.security.UserPrincipal;
import it.ddbdev.ticketsystem.service.AuthorityService;
import it.ddbdev.ticketsystem.service.AvatarService;
import it.ddbdev.ticketsystem.service.FileService;
import it.ddbdev.ticketsystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/user")
@SecurityRequirement(name = "ddbdev_ticketsystem")
@Slf4j
public class UserController {

    private final UserService userService;

    private final AuthorityService authorityService;

    private final FileService fileService;

    private final AvatarService avatarService;

    @Autowired
    public UserController(UserService userService, AuthorityService authorityService, FileService fileService, AvatarService avatarService) {
        this.userService = userService;
        this.authorityService = authorityService;
        this.fileService = fileService;
        this.avatarService = avatarService;
    }


    @Value("${avatar.width}")
    private int avatarWidth;

    @Value("${avatar.height}")
    private int avatarHeight;

    @Value("${avatar.weight}")
    private long avatarWeight;

    @Value("${avatar.extensions}")
    private String[] avatarExtensions;


    @PutMapping("update-authority")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<?> updateAuth(@RequestBody UpdateUserAuthority request){

        Optional<User> foundUser = userService.getUserById(request.getUserId());

        if (!foundUser.isPresent()){
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        Set<Authority> authorities = authorityService.getAuthorityFromSet(request.getAuthorityIds());

        if (authorities.isEmpty()){
            return new ResponseEntity<>("Authorities not found", HttpStatus.NOT_FOUND);
        }

        foundUser.get().setAuthorities(authorities);
        return new ResponseEntity<>("User authorities modified", HttpStatus.OK);

    }

    @GetMapping("{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserAuthorities(@PathVariable Long userId){
        UserResponse userResponse = userService.getUserResponse(userId);

        if (userResponse == null)
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        List<AuthorityUserResponse> authorityLists = authorityService.getAuthoritiesByUser(userId);
        UserAuthoritiesResponse uar = new UserAuthoritiesResponse();
        uar.setAuthorities(authorityLists);
        uar.setUserResponse(userResponse);

        return new ResponseEntity<>(uar, HttpStatus.OK);
    }

    @GetMapping("/get-me")
    public ResponseEntity<?> getMe(@CurrentUser UserPrincipal user)
    {
        Optional<User> foundUser = userService.getUserById(user.getId());

        return new ResponseEntity<>(userService.getMe(foundUser.get().getId()), HttpStatus.OK);

    }

    /*
    @PutMapping(value = "update-me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<?> updateMe(
            @CurrentUser UserPrincipal user,
            @RequestParam(name = "username") @NotBlank @Size(max = 15, min=5) String username,
            @RequestParam(name = "email") @NotBlank @Email String email,
            @RequestParam(name = "bio") String bio,
            @RequestPart(name = "file") MultipartFile file) throws IOException {


        User foundUser = userService.getUserById(user.getId()).get();

        if (userService.existsByUsername(username))
            return new ResponseEntity<>("Username already in use", HttpStatus.FORBIDDEN);

        if (userService.existsByUsername(email))
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);

        //Check extensions
        if(!fileService.checkExtension(file, avatarExtensions))
            return new ResponseEntity<>("File type not allowed", HttpStatus.FORBIDDEN);

        //Check file size (bytes)
        if (!fileService.checkSize(file, avatarWeight))
            return new ResponseEntity<>("File too big or empty", HttpStatus.FORBIDDEN);

        //Check dimensions
        if (!fileService.checkWeight(fileService.fromMultipartFileToBufferedImage(file), avatarWidth, avatarHeight))
            return new ResponseEntity<>("Wrong avatar size", HttpStatus.FORBIDDEN);

        Avatar avatar = avatarService.createAvatarFromMultipartFile(file);

        avatarService.addAvatar(avatar);

        foundUser.setUsername(username);
        foundUser.setEmail(email);
        foundUser.setBio(bio);
        foundUser.setAvatar(avatar);

        return new ResponseEntity<>("Your profile has been updated", HttpStatus.OK);
    }
    */


    @PutMapping(value = "update-me")
    @Transactional
    public ResponseEntity<?> updateMe(
            @CurrentUser UserPrincipal user,
            @RequestBody @Valid UpdateMeRequest request) {


        User foundUser = userService.getUserById(user.getId()).get();

        if (userService.existsByUsername(request.getUsername()) && !request.getUsername().equals(foundUser.getUsername()))
            return new ResponseEntity<>("Username already in use", HttpStatus.FORBIDDEN);

        if (userService.existsByUsername(request.getEmail()) && !request.getEmail().equalsIgnoreCase(foundUser.getEmail()))
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);


        foundUser.setUsername(request.getUsername());
        foundUser.setEmail(request.getEmail().toLowerCase());
        foundUser.setBio(request.getBio());

        return new ResponseEntity<>("Your profile has been updated", HttpStatus.OK);
    }

    @PutMapping(value = "update-avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<?> updateMe(@CurrentUser UserPrincipal user, @RequestParam MultipartFile file) throws IOException {

        User foundUser = userService.getUserById(user.getId()).get();

        //Check extensions
        if(!fileService.checkExtension(file, avatarExtensions))
            return new ResponseEntity<>("File type not allowed", HttpStatus.FORBIDDEN);

        //Check file size (bytes)
        if (!fileService.checkSize(file, avatarWeight))
            return new ResponseEntity<>("File too big or empty", HttpStatus.FORBIDDEN);

        //Check dimensions
        if (!fileService.checkWeight(fileService.fromMultipartFileToBufferedImage(file), avatarWidth, avatarHeight))
            return new ResponseEntity<>("Wrong avatar size", HttpStatus.FORBIDDEN);

        Avatar avatar = avatarService.createAvatarFromMultipartFile(file);
        if (foundUser.getAvatar() != null){
            avatar.setId(foundUser.getAvatar().getId());
            avatarService.addAvatar(avatar);
        }
        else {
            avatarService.addAvatar(avatar);
            foundUser.setAvatar(avatar);
        }

        return new ResponseEntity<>("Your profile avatar has been updated", HttpStatus.OK);
    }

    @DeleteMapping("/remove-avatar")
    @Transactional
    public ResponseEntity<?> deleteAvatar(@CurrentUser UserPrincipal user){

        String msg = "Your avatar has been removed";
        User foundUser = userService.getUserById(user.getId()).get();

        if (foundUser.getAvatar() != null){
            long avatarTmp = foundUser.getAvatar().getId();
            avatarService.deleteAvatar(avatarTmp);
            foundUser.setAvatar(null);
        }
        else
            msg = "No avatar to remove";


        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    /*
    @GetMapping("/get-me")
    public ResponseEntity<?> getMe(@CurrentSecurityContext(expression="authentication") Authentication authentication)
    {
        User up = UserPrincipal.createUserFromUserPrincipal((UserPrincipal) authentication.getPrincipal());

        return new ResponseEntity<>(userService.getMe(up.getId()), HttpStatus.OK);
    }
    */
}
