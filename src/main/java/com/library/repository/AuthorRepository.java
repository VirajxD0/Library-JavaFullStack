package com.library.repository;

import com.library.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author Repository - Spring Data JPA Repository for Author entity
 * 
 * JpaRepository automatically provides:
 * - findAll(), findById(), save(), delete(), count()
 * - flush(), saveAndFlush(), deleteInBatch()
 * - Custom queries via method naming convention
 */
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    /**
     * Find authors by last name (case-insensitive partial match)
     * Spring Data JPA generates: SELECT * FROM authors WHERE LOWER(last_name) LIKE LOWER('%name%')
     */
    List<Author> findByLastNameContainingIgnoreCase(String lastName);

    /**
     * Find authors by first name (case-insensitive partial match)
     */
    List<Author> findByFirstNameContainingIgnoreCase(String firstName);
}
