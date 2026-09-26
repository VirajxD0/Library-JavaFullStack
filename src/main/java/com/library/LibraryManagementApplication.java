package com.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Library Management System - Main Application Entry Point
 * 
 * @SpringBootApplication combines:
 *   @Configuration - Marks this class as a source of bean definitions
 *   @EnableAutoConfiguration - Enables Spring Boot's auto-configuration
 *   @ComponentScan - Scans for components in this package and sub-packages
 * 
 * This class bootstraps the entire application including:
 * - Spring MVC (for REST APIs and JSP views)
 * - Spring Data JPA (for database operations via Hibernate)
 * - Embedded Tomcat server (runs on port 8080 by default)
 */
@SpringBootApplication
public class LibraryManagementApplication {

    public static void main(String[] args) {
        // SpringApplication.run() starts the entire Spring context
        // It configures the embedded Tomcat, scans for beans, and starts the application
        SpringApplication.run(LibraryManagementApplication.class, args);
        System.out.println("============================================");
        System.out.println("  Library Management System Started!        ");
        System.out.println("  JSP Views:  http://localhost:8080/        ");
        System.out.println("  REST API:   http://localhost:8080/api/v1/ ");
        System.out.println("  H2 Console: http://localhost:8080/h2-console ");
        System.out.println("============================================");
    }
}
