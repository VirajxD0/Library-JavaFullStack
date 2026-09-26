package com.library.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * User Entity - Core authentication entity for Spring Security + JWT
 * 
 * Demonstrates:
 * - @Entity mapping with unique constraints (username, email)
 * - @Enumerated for Role enum storage
 * - Relationship to Member (One-to-One) - each login user linked to a library member OR standalone
 * - BCrypt hashed password (never store plain text)
 * 
 * Interview Highlight:
 * - Explain why password is BCrypt hashed (one-way, salted, adaptive)
 * - Why username/email are unique (business rule + DB constraint)
 * - How Role enum maps to Spring Security GrantedAuthority
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username; // used for login (Spring Security principal)

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, length = 255)
    private String password; // BCrypt hashed, length 60+ so 255 safe

    @Enumerated(EnumType.STRING) // Store enum as String "ROLE_USER" not ordinal 0/1
    @Column(nullable = false, length = 20)
    private Role role = Role.ROLE_USER;

    @Column(nullable = false)
    private boolean enabled = true; // for account activation/ban

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Optional link to Member profile (if user is also a library member)
    // Fetch LAZY to avoid loading member unless needed
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id", unique = true)
    private Member member;

    // Default constructor required by JPA
    public User() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public User(String username, String email, String password, Role role) {
        this();
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', role=" + role + "}";
    }
}
