package com.library.repository;

import com.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Book Repository - Spring Data JPA Repository for Book entity
 * 
 * Spring Data JPA automatically generates the implementation at runtime.
 * We just declare the method signatures - Spring creates the SQL queries.
 * 
 * JpaRepository<Book, Long> provides:
 * - findAll(), findById(), save(), delete(), count(), etc. (CRUD operations)
 * - Pagination and sorting support
 * - Custom query methods via method naming convention
 * 
 * Demonstrates:
 * - Derived query methods (Spring generates SQL from method name)
 * - @Query annotation for custom JPQL queries
 * - @Param for named parameters
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    // ==================== Derived Query Methods ====================
    // Spring Data JPA parses the method name and generates the query automatically
    // Pattern: findBy + FieldName + Condition

    /**
     * Find books by title containing the search keyword (case-insensitive)
     * Generated SQL: SELECT * FROM books WHERE LOWER(title) LIKE LOWER('%keyword%')
     */
    List<Book> findByTitleContainingIgnoreCase(String title);

    /**
     * Find books by category ID
     * Generated SQL: SELECT * FROM books WHERE category_id = ?
     */
    List<Book> findByCategoryCategoryId(Long categoryId);

    /**
     * Find books that have available copies greater than 0
     * Generated SQL: SELECT * FROM books WHERE available_copies > 0
     */
    List<Book> findByAvailableCopiesGreaterThan(int count);

    /**
     * Find books by ISBN (exact match)
     * Generated SQL: SELECT * FROM books WHERE isbn = ?
     */
    Optional<Book> findByIsbn(String isbn);

    // ==================== Custom JPQL Queries ====================

    /**
     * Search books by title OR ISBN (flexible search)
     * JPQL (Java Persistence Query Language) works on entity fields, not table columns.
     * CONCAT adds the % wildcards because JPQL parameters cannot be
     * embedded inside a LIKE literal (%:keyword would match literally).
     */
    @Query("SELECT b FROM Book b WHERE b.title LIKE CONCAT('%', :keyword, '%') OR b.isbn LIKE CONCAT('%', :keyword, '%')")
    List<Book> searchBooks(@Param("keyword") String keyword);

    /**
     * Find book with its authors loaded (eager fetch to avoid LazyInitializationException)
     * LEFT JOIN FETCH ensures books without authors are also returned
     */
    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.authors WHERE b.bookId = :id")
    Optional<Book> findByIdWithAuthors(@Param("id") Long id);

    /**
     * Find books by author ID (uses the join table book_authors)
     */
    @Query("SELECT b FROM Book b JOIN b.authors a WHERE a.authorId = :authorId")
    List<Book> findByAuthorId(@Param("authorId") Long authorId);
}
