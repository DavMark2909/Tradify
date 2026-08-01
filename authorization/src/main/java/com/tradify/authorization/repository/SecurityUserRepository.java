package com.tradify.authorization.repository;

import com.tradify.authorization.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SecurityUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
