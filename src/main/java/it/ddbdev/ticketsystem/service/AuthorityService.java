package it.ddbdev.ticketsystem.service;

import it.ddbdev.ticketsystem.entity.Authority;
import it.ddbdev.ticketsystem.payload.response.AuthorityUserResponse;
import it.ddbdev.ticketsystem.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    public Optional<Authority> getById(Long id){
        return authorityRepository.findById(id);
    }

    public Optional<Authority> getByAuthorityName(String authorityName){
        return authorityRepository.findByAuthorityName(authorityName);
    }

    public void addAuthority(Authority authority){
       authorityRepository.save(authority);
    }

    public List<Authority> getAll(){
        return authorityRepository.findAll();
    }

    public List<Authority> getAllVisible(){
        return authorityRepository.findByVisibleTrue();
    }

    public boolean existsByAuthorityName(String authorityName){
        return authorityRepository.existsByAuthorityName(authorityName);
    }

    public Optional<Authority> findByDefaultAuthorityTrue() {
        return authorityRepository.findByDefaultAuthorityTrue();
    }

    public Set<Authority> getAuthorityFromSet(Set<Long> authorityIds){
        return authorityRepository.findByIdIn(authorityIds);
    }

    public List<AuthorityUserResponse> getAuthoritiesByUser(Long id){
        return authorityRepository.getAuthoritiesByUser(id);
    }
}
