package com.library.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * AuthorDTO - Data Transfer Object for Author entity
 */
public class AuthorDTO {

    private Long authorId;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Email must be valid")
    private String email;

    private String biography;

    // Default constructor
    public AuthorDTO() {}

    // Getters and Setters
    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getBiography() { return biography; }
    public void setBiography(String biography) { this.biography = biography; }

    /**
     * Helper to get full name - Core Java string concatenation
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
