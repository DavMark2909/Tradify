package com.tradify.application.security;

import com.tradify.application.entity.User;
import com.tradify.application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class JwtConverter extends Converter<Jwt, AbstractAuthenticationToken> {

    private final UserRepository userRepository;

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        String username = jwt.getClaimAsString("username");
        String name = jwt.getClaimAsString("name");
        String lastname = jwt.getClaimAsString("lastname");

        userRepository.findByUsername(username).orElseGet(() -> {

            User user = new User();
            user.setUsername(username);
            user.setName(name);
            user.setLastName(lastname);
            return userRepository.save(user);
        });

        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        Collection<GrantedAuthority> authorities = authoritiesConverter.convert(jwt);

        return new JwtAuthenticationToken(jwt, authorities, username);
    }

    @Override
    public <U> Converter<Jwt, @Nullable U> andThen(Converter<? super AbstractAuthenticationToken, ? extends @Nullable U> after) {
        return Converter.super.andThen(after);
    }
}
