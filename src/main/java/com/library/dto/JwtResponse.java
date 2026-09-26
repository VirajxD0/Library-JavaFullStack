package com.library.dto;

/**
 * JwtResponse - Returned after successful login (REST API)
 * 
 * Contains JWT token and user info.
 * Frontend stores token in localStorage and sends as Authorization: Bearer <token>
 */
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String role;

    public JwtResponse(String token, Long id, String username, String email, String role) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getType() { return type; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public Long getId() { return id; }
}
