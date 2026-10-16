package com.library.dto;

/**
 * BookSearchCriteria - multi-criteria book search parameters.
 * All fields optional; null/blank means "don't filter on this field".
 * Binds from query params on GET /api/v1/books/search/advanced.
 */
public class BookSearchCriteria {

    private String keyword;      // matches title or ISBN (case-insensitive)
    private String title;
    private String isbn;
    private Long categoryId;
    private Long authorId;
    private String language;
    private Integer publicationYear;
    private Boolean availableOnly;

    public BookSearchCriteria() {}

    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public Integer getPublicationYear() { return publicationYear; }
    public void setPublicationYear(Integer publicationYear) { this.publicationYear = publicationYear; }

    public Boolean getAvailableOnly() { return availableOnly; }
    public void setAvailableOnly(Boolean availableOnly) { this.availableOnly = availableOnly; }
}
