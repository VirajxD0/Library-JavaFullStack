package com.library.repository;

import com.library.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Member Repository - Spring Data JPA Repository for Member entity
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * Find member by email (unique identifier)
     */
    Optional<Member> findByEmail(String email);

    /**
     * Find all active members
     * Generated SQL: SELECT * FROM members WHERE active = true
     */
    List<Member> findByActiveTrue();

    /**
     * Find members by last name
     */
    List<Member> findByLastNameContainingIgnoreCase(String lastName);
}
