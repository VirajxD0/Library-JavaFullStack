package com.library.entity;

/**
 * Role enum - RBAC roles for the Library Management System.
 *
 * Canonical roles (Project 2 spec):
 * - ROLE_LIBRARIAN: manages books, members, categories, sees all borrows
 * - ROLE_MEMBER: browses/searches books, borrows and returns
 *
 * Legacy aliases kept for backward compatibility with existing seed data:
 * - ROLE_ADMIN == librarian privileges
 * - ROLE_USER == member privileges
 *
 * SecurityConfig grants librarian endpoints to ADMIN+LIBRARIAN and
 * member endpoints to USER+MEMBER+ADMIN+LIBRARIAN.
 */
public enum Role {
    ROLE_LIBRARIAN, // Librarian (canonical)
    ROLE_MEMBER,    // Member (canonical)
    ROLE_ADMIN,     // Legacy alias of LIBRARIAN
    ROLE_USER;      // Legacy alias of MEMBER

    /**
     * Get role without prefix (for display)
     */
    public String getDisplayName() {
        return this.name().replace("ROLE_", "");
    }

    /**
     * True if this role carries librarian privileges.
     */
    public boolean isLibrarian() {
        return this == ROLE_LIBRARIAN || this == ROLE_ADMIN;
    }

    /**
     * True if this role carries member privileges (librarians inherit them).
     */
    public boolean isMember() {
        return true; // every role may browse/borrow; URL rules refine this
    }
}
