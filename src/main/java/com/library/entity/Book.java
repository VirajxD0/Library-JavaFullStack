package com.library.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Book Entity - The core entity of the Library Management System
 * 
 * Demonstrates multiple JPA relationships:
 * 
 * 1. @ManyToOne with Category - Many books belong to one category
 *    - Foreign key: category_id in books table
 *    - LAZY loading (default) - category loaded only when accessed
 * 
 * 2. @ManyToOne with Publisher - Many books are published by one publisher
 *    - Foreign key: publisher_id in books table
 * 
 * 3. @ManyToMany with Author - Many books have many authors (bidirectional)
 *    - Join table: book_authors (book_id, author_id)
 *    - CascadeType.ALL - when a book is saved, its authors are also persisted
 *    - Hibernate manages the relationship through the join table
 */
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long bookId;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, unique = true, length = 20)
    private String isbn; // International Standard Book Number

    @Column(name = "publication_year")
    private Integer publicationYear;

    @Column(length = 50)
    private String edition;

    @Column(name = "total_copies", nullable = false)
    private int totalCopies;

    @Column(name = "available_copies", nullable = false)
    private int availableCopies;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 30)
    private String language;

    // Many-to-One: Many books belong to one Category
    @ManyToOne(fetch = FetchType.EAGER) // EAGER: Load category immediately with book
    @JoinColumn(name = "category_id") // Foreign key column name
    private Category category;

    // Many-to-One: Many books are published by one Publisher
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    // Many-to-Many: A book can have multiple authors (bidirectional)
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "book_authors", // Join table name
        joinColumns = @JoinColumn(name = "book_id"), // FK for this entity
        inverseJoinColumns = @JoinColumn(name = "author_id") // FK for the other entity
    )
    private Set<Author> authors = new HashSet<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Default constructor
    public Book() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Full constructor for quick creation
    public Book(String title, String isbn, Integer publicationYear, int totalCopies, int availableCopies,
                String description, String language, Category category, Publisher publisher) {
        this();
        this.title = title;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
        this.description = description;
        this.language = language;
        this.category = category;
        this.publisher = publisher;
    }

    // ==================== Many-to-Many Helper Methods ====================

    /**
     * Add an author to this book (demonstrates bidirectional relationship management)
     * Core Java: Using Set to avoid duplicate authors
     */
    public void addAuthor(Author author) {
        this.authors.add(author);
    }

    /**
     * Remove an author from this book
     */
    public void removeAuthor(Author author) {
        this.authors.remove(author);
    }

    // ==================== Business Logic Methods ====================

    /**
     * Check if the book is available for borrowing
     * Core Java: Simple conditional logic
     */
    public boolean isAvailable() {
        return this.availableCopies > 0;
    }

    /**
     * Decrease available copies when a book is borrowed
     * Core Java: Arithmetic operations
     */
    public void decreaseAvailableCopies() {
        if (this.availableCopies > 0) {
            this.availableCopies--;
        }
    }

    /**
     * Increase available copies when a book is returned
     */
    public void increaseAvailableCopies() {
        if (this.availableCopies < this.totalCopies) {
            this.availableCopies++;
        }
    }

    // ==================== Getters and Setters ====================
    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Integer getPublicationYear() { return publicationYear; }
    public void setPublicationYear(Integer publicationYear) { this.publicationYear = publicationYear; }

    public String getEdition() { return edition; }
    public void setEdition(String edition) { this.edition = edition; }

    public int getTotalCopies() { return totalCopies; }
    public void setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; }

    public int getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Publisher getPublisher() { return publisher; }
    public void setPublisher(Publisher publisher) { this.publisher = publisher; }

    public Set<Author> getAuthors() { return authors; }
    public void setAuthors(Set<Author> authors) { this.authors = authors; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Book{bookId=" + bookId + ", title='" + title + "', isbn='" + isbn + "'}";
    }
}
