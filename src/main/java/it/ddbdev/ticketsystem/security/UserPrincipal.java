package it.ddbdev.ticketsystem.security;


import com.fasterxml.jackson.annotation.JsonIgnore;

import it.ddbdev.ticketsystem.entity.Authority;
import it.ddbdev.ticketsystem.entity.User;
import it.ddbdev.ticketsystem.payload.response.JwtAuthenticationResponse;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

    private long id;
    private String username;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean enabled;

    public UserPrincipal(
            long id,
            String username,
            String email,
            String password,
            Collection<? extends GrantedAuthority> authorities,
            Boolean enabled
    ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.enabled = enabled;
    }

    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = user.getAuthorities().stream().map(role ->
                new SimpleGrantedAuthority(role.getAuthorityName())
        ).collect(Collectors.toList());

        return new UserPrincipal(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities,
                user.isEnabled()
        );
    }

    public static User createUserFromUserPrincipal(UserPrincipal up) {
        return new User(
                up.getId(),
                up.getUsername(),
                up.getEmail(),
                up.getAuthorities().stream().map(a -> new Authority(a.getAuthority())).collect(Collectors.toSet())
        );
    }

    public static JwtAuthenticationResponse createJwtAuthenticationResponseFromUserPrincipal(UserPrincipal up, String token) {
        return new JwtAuthenticationResponse(
                up.getId(),
                up.getUsername(),
                up.getEmail(),
                up.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toSet()),
                token
        );
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Serial
    private static final long serialVersionUID = -3186064719257201546L;

}
