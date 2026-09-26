package com.library.repository;

import com.library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserRepository - Spring Data JPA for User entity
 * 
 * Methods used by:
 * - UserDetailsServiceImpl.loadUserByUsername() (Spring Security)
 * - AuthService (duplicate check on register)
 * - JWT generation (findByUsername after authentication)
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find by username - primary login identifier
    Optional<User> findByUsername(String username);

    // Find by email - alternative login + duplicate check
    Optional<User> findByEmail(String email);

    // Existence checks for registration validation
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
