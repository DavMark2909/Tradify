package com.tradify.authorization.security;

import org.springframework.security.core.GrantedAuthority;

public class SecurityRole implements GrantedAuthority {
    private String role;

    public SecurityRole(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role;
    }
}
