package com.library.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * CategoryDTO - Data Transfer Object for Category entity
 */
public class CategoryDTO {

    private Long categoryId;

    @NotBlank(message = "Category name is required")
    private String name;

    private String description;

    // Default constructor
    public CategoryDTO() {}

    // Getters and Setters
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
