package com.tradify.authorization.security;

import com.tradify.authorization.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

@Getter
public class SecurityUser implements UserDetails {

    private final String username;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final long id;
    private final Collection<? extends GrantedAuthority> authorities;

    public SecurityUser(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();

        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.id = user.getId();

        this.authorities = user.getRoles().stream()
                .map(role -> new SecurityRole(role.getName()))
                .toList();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}