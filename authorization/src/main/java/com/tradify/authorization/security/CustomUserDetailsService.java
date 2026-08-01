package com.tradify.authorization.security;

import com.tradify.authorization.dto.UserRegistrationDto;
import com.tradify.authorization.entity.Role;
import com.tradify.authorization.entity.User;
import com.tradify.authorization.repository.RoleRepository;
import com.tradify.authorization.repository.SecurityUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final SecurityUserRepository securityUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(SecurityUserRepository securityUserRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.securityUserRepository = securityUserRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return securityUserRepository.findByUsername(username).
                map(SecurityUser::new).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public Optional<User> registerUser(UserRegistrationDto userRegistrationDto) {
        User user = new User();
        user.setUsername(userRegistrationDto.getUsername());

        String encodedPassword = passwordEncoder.encode(userRegistrationDto.getPassword());
        user.setPassword(encodedPassword);

        user.setFirstName(userRegistrationDto.getFirstName());
        user.setLastName(userRegistrationDto.getLastName());

        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Critical Error: Default Role not found in database."));

        user.setRoles(Set.of(defaultRole));

        return Optional.of(securityUserRepository.save(user));
    }
}
