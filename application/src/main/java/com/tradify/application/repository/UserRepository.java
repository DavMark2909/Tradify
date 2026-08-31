package com.tradify.application.repository;

import com.tradify.application.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Set<User> findAllByUsernameIn(Set<String> usernames);
}
