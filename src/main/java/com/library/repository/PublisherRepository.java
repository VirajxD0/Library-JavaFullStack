package com.library.repository;

import com.library.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Publisher Repository - Spring Data JPA Repository for Publisher entity
 */
@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    /**
     * Find publisher by name (case-insensitive partial match)
     */
    Optional<Publisher> findByNameContainingIgnoreCase(String name);
}
