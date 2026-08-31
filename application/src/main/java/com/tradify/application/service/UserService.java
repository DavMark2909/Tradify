package com.tradify.application.service;

import com.tradify.application.entity.User;
import com.tradify.application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Could not find a user with id: " + id));
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public Set<User> findAllUsersByUsernameIn(Set<String> usernames) {
        return userRepository.findAllByUsernameIn(usernames);
    }

    public void saveAll(Set<User> users) {
        userRepository.saveAll(users);
    }
}
