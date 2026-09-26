package com.library.entity;

/**
 * Role Enum - Defines user roles for RBAC (Role-Based Access Control)
 * 
 * ROLE_USER: Can view books, borrow/return books, view dashboard
 * ROLE_ADMIN: Has all USER permissions + can manage books, members, categories
 * 
 * Spring Security expects roles to start with "ROLE_" prefix.
 * In @PreAuthorize or .hasRole("ADMIN") Spring automatically adds prefix.
 */
public enum Role {
    ROLE_USER,   // Regular library user
    ROLE_ADMIN;  // Librarian / Administrator

    /**
     * Get role without prefix (for display)
     */
    public String getDisplayName() {
        return this.name().replace("ROLE_", "");
    }
}
