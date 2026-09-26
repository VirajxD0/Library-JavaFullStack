package com.library.service;

import com.library.dto.BookDTO;
import java.util.List;

/**
 * BookService Interface - Defines the business operations for books
 * 
 * Interface-based design:
 * - Defines a contract for what operations are available
 * - Service implementation class provides the actual logic
 * - Controllers depend on the interface, not the implementation
 * - This is called "Programming to an Interface" (Core Java OOP principle)
 */
public interface BookService {

    /**
     * Get all books with pagination
     * @param page page number (0-indexed)
     * @param size number of items per page
     * @return list of BookDTOs
     */
    List<BookDTO> getAllBooks(int page, int size);

    /**
     * Get a single book by its ID
     * @param id book ID
     * @return BookDTO
     * @throws ResourceNotFoundException if book not found
     */
    BookDTO getBookById(Long id);

    /**
     * Create a new book
     * @param bookDTO book data from the client
     * @return created BookDTO with generated ID
     */
    BookDTO createBook(BookDTO bookDTO);

    /**
     * Update an existing book
     * @param id book ID to update
     * @param bookDTO updated book data
     * @return updated BookDTO
     */
    BookDTO updateBook(Long id, BookDTO bookDTO);

    /**
     * Delete a book by ID
     * @param id book ID to delete
     */
    void deleteBook(Long id);

    /**
     * Search books by keyword (title or ISBN)
     * @param keyword search term
     * @return list of matching BookDTOs
     */
    List<BookDTO> searchBooks(String keyword);

    /**
     * Get books that have available copies
     * @return list of available BookDTOs
     */
    List<BookDTO> getAvailableBooks();

    /**
     * Get books by category
     * @param categoryId category ID
     * @return list of BookDTOs
     */
    List<BookDTO> getBooksByCategory(Long categoryId);
}
