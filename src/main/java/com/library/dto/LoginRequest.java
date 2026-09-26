package com.library.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * LoginRequest - DTO for user login
 * Supports login via username OR email + password
 */
public class LoginRequest {

    @NotBlank(message = "Username or email is required")
    private String username; // can be username or email

    @NotBlank(message = "Password is required")
    private String password;

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
