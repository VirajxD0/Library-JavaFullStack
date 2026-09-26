package com.library.exception;

/**
 * DuplicateResourceException - Custom exception for duplicate resource creation
 * 
 * Example: Trying to create a book with an ISBN that already exists
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
