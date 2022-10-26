package it.ddbdev.ticketsystem.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.ddbdev.ticketsystem.entity.Authority;
import it.ddbdev.ticketsystem.payload.request.AuthorityRequest;
import it.ddbdev.ticketsystem.service.AuthorityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RequestMapping("/authority")
@RestController
@Slf4j
@Validated
@SecurityRequirement(name = "ddbdev_ticketsystem")
public class AuthorityController {

    @Autowired
    private AuthorityService authorityService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addAuthority(@RequestBody @Valid AuthorityRequest request){

        Optional<Authority> getAuthority = authorityService.getByAuthorityName(request.getAuthorityName());

        if (getAuthority.isEmpty()){
            String authorityToPass = request.getAuthorityName().trim().toUpperCase();
            Authority authority = new Authority();
            authority.setAuthorityName(authorityToPass);
            authorityService.addAuthority(authority);

            return ResponseEntity.ok().body("Authority " + authority.getAuthorityName() + " has been added.");
        }
        else {
            return ResponseEntity.ok().body("Authority " + getAuthority.get().getAuthorityName() + " is already present.");
        }
    }

    @GetMapping("{visible}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getVisibileAuthorities(@PathVariable boolean visible){
        List<Authority> authorityList;


        if (visible)
            authorityList = authorityService.getAllVisible();
        else
            authorityList = authorityService.getAll();


        if (authorityList.isEmpty()){
            return new ResponseEntity<>("No authorities found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(authorityList,HttpStatus.OK);
    }

    @PutMapping("{id}")
    @Transactional //Effettua l'update a fine metodo
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateAuthority (@PathVariable Long id){

        Optional<Authority> foundAuth = authorityService.getById(id);

        if (!foundAuth.isPresent()){
            return new ResponseEntity<>("Authority not found", HttpStatus.NOT_FOUND);
        }

        foundAuth.get().setVisible(!foundAuth.get().isVisible());

        return new ResponseEntity<>("Authority "+ foundAuth.get().getAuthorityName() + " visibility has been set to " + foundAuth.get().isVisible(), HttpStatus.OK);
    }
}
