package com.library.service;

import com.library.dto.BookDTO;
import com.library.dto.BookSearchCriteria;
import com.library.dto.PagedResponse;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * BookService Interface - Defines the business operations for books
 */
public interface BookService {

    /**
     * Get all books with pagination (legacy List form, kept for JSP callers).
     */
    List<BookDTO> getAllBooks(int page, int size);

    /**
     * Get all books as a real page (Pageable + Sort honored).
     */
    PagedResponse<BookDTO> getAllBooks(Pageable pageable);

    /**
     * Multi-criteria search with pagination and sorting.
     */
    PagedResponse<BookDTO> searchBooks(BookSearchCriteria criteria, Pageable pageable);

    BookDTO getBookById(Long id);
    BookDTO createBook(BookDTO bookDTO);
    BookDTO updateBook(Long id, BookDTO bookDTO);
    void deleteBook(Long id);

    /**
     * Legacy single-keyword search (title or ISBN), kept for JSP callers.
     */
    List<BookDTO> searchBooks(String keyword);

    List<BookDTO> getAvailableBooks();
    List<BookDTO> getBooksByCategory(Long categoryId);
}
