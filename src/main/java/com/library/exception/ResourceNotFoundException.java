package com.library.exception;

/**
 * ResourceNotFoundException - Custom exception for when a requested entity is not found
 * 
 * This is a common pattern in REST APIs:
 * - When a user requests /api/v1/books/999 and book 999 doesn't exist
 * - We throw this exception
 * - The GlobalExceptionHandler catches it and returns a 404 response
 * 
 * Extending RuntimeException makes it an unchecked exception (no try-catch needed)
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message); // Pass message to parent RuntimeException
    }

    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " not found with id: " + id);
    }
}
