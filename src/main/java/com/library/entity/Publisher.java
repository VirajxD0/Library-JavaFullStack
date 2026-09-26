package com.library.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Publisher Entity - Represents book publishers
 * 
 * Demonstrates:
 * - @OneToMany relationship with Book (one publisher can publish many books)
 * - Simple entity mapping with JPA annotations
 */
@Entity
@Table(name = "publishers")
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "publisher_id")
    private Long publisherId;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 300)
    private String address;

    @Column(length = 200)
    private String website;

    @Column(length = 20)
    private String phone;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Default constructor
    public Publisher() {
        this.createdAt = LocalDateTime.now();
    }

    // Constructor for quick creation
    public Publisher(String name, String address, String website) {
        this();
        this.name = name;
        this.address = address;
        this.website = website;
    }

    // Getters and Setters
    public Long getPublisherId() { return publisherId; }
    public void setPublisherId(Long publisherId) { this.publisherId = publisherId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return "Publisher{publisherId=" + publisherId + ", name='" + name + "'}";
    }
}
