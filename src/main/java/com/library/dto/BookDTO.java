package com.library.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * BookDTO - Data Transfer Object for Book entity
 * 
 * DTOs are used to:
 * 1. Hide internal entity structure from API consumers
 * 2. Control what data is exposed (e.g., don't expose internal IDs)
 * 3. Validate input using Bean Validation annotations
 * 4. Decouple API contract from database schema
 * 
 * This is a key design pattern in REST API development.
 */
public class BookDTO {

    private Long bookId;

    @NotBlank(message = "Title is required") // Hibernate Validator: field cannot be blank
    private String title;

    @NotBlank(message = "ISBN is required")
    @Size(min = 10, max = 17, message = "ISBN must be between 10-17 characters")
    private String isbn;

    private Integer publicationYear;
    private String edition;

    @Min(value = 1, message = "Total copies must be at least 1")
    private int totalCopies;

    private int availableCopies;
    private String description;
    private String language;

    // Category info (flattened for API response)
    private Long categoryId;
    private String categoryName;

    // Publisher info (flattened for API response)
    private Long publisherId;
    private String publisherName;

    // Authors list (flattened for API response)
    private List<AuthorDTO> authors;

    // Default constructor
    public BookDTO() {}

    // Getters and Setters
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

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public Long getPublisherId() { return publisherId; }
    public void setPublisherId(Long publisherId) { this.publisherId = publisherId; }

    public String getPublisherName() { return publisherName; }
    public void setPublisherName(String publisherName) { this.publisherName = publisherName; }

    public List<AuthorDTO> getAuthors() { return authors; }
    public void setAuthors(List<AuthorDTO> authors) { this.authors = authors; }
}
