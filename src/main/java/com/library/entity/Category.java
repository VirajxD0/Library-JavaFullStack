package com.library.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Category Entity - Represents book categories (e.g., Fiction, Technology)
 * 
 * JPA Annotations Explained:
 * @Entity - Marks this class as a JPA entity (maps to a database table)
 * @Table - Specifies the table name in the database
 * @Id - Marks the primary key field
 * @GeneratedValue - Auto-generates the primary key value
 * @Column - Maps to a specific column in the table
 * 
 * Hibernate will create a 'categories' table with these columns:
 * category_id (PK), name, description, created_at, updated_at
 */
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT in MySQL
    @Column(name = "category_id")
    private Long categoryId;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Default constructor (required by JPA)
    public Category() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Constructor with name and description (used for initialization)
    public Category(String name, String description) {
        this(); // Call default constructor to set timestamps
        this.name = name;
        this.description = description;
    }

    // Getters and Setters
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Category{categoryId=" + categoryId + ", name='" + name + "'}";
    }
}
