package com.library.exception;

/**
 * BookNotAvailableException - borrowing attempted with no copies left.
 * Maps to 409 Conflict via GlobalExceptionHandler.
 */
public class BookNotAvailableException extends RuntimeException {

    public BookNotAvailableException(Long bookId) {
        super("Book is not available for borrowing (id=" + bookId + "). No copies left.");
    }

    public BookNotAvailableException(String message) {
        super(message);
    }
}
