package com.tradify.application.repository;

import com.tradify.application.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Set<User> findAllByUsernameIn(Set<String> usernames);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.companyProfile WHERE u.username = :username")
    Optional<User> findByUsernameWithCompany(@Param("username") String username);
}
