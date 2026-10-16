package com.library.rest;

import com.library.dto.ApiResponse;
import com.library.dto.BookDTO;
import com.library.dto.BookSearchCriteria;
import com.library.dto.PagedResponse;
import com.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * BookRestController - REST API for Book operations.
 *
 * Pagination/sorting: GET /api/v1/books?page=0&size=10&sort=title,asc
 * Multi-criteria search: GET /api/v1/books/search/advanced?keyword=&title=&categoryId=&authorId=&language=&publicationYear=&availableOnly=
 *
 * Authorization (librarian vs member):
 * - Reads: any authenticated user (librarian or member); two endpoints public.
 * - Writes: librarian only (ROLE_LIBRARIAN / legacy ROLE_ADMIN).
 */
@RestController
@RequestMapping("/api/v1/books")
@CrossOrigin(origins = "*") // Allow requests from any origin (for development)
public class BookRestController {

    @Autowired
    private BookService bookService;

    /**
     * GET /api/v1/books - Paginated + sorted book list.
     * Example: /api/v1/books?page=0&size=10&sort=title,asc
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<BookDTO>>> getAllBooks(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(bookService.getAllBooks(pageable)));
    }

    /**
     * GET /api/v1/books/{id} - Get a single book by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookDTO>> getBookById(@PathVariable Long id) {
        BookDTO book = bookService.getBookById(id);
        return ResponseEntity.ok(ApiResponse.success(book));
    }

    /**
     * POST /api/v1/books - Create a new book (librarian only)
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
    public ResponseEntity<ApiResponse<BookDTO>> createBook(
            @Valid @RequestBody BookDTO bookDTO) {
        BookDTO created = bookService.createBook(bookDTO);
        // HTTP 201 Created is the standard status for successful resource creation
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Book created successfully"));
    }

    /**
     * PUT /api/v1/books/{id} - Update an existing book (librarian only)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
    public ResponseEntity<ApiResponse<BookDTO>> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookDTO bookDTO) {
        BookDTO updated = bookService.updateBook(id, bookDTO);
        return ResponseEntity.ok(ApiResponse.success(updated, "Book updated successfully"));
    }

    /**
     * DELETE /api/v1/books/{id} - Delete a book (librarian only)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Book deleted successfully"));
    }

    /**
     * GET /api/v1/books/search?keyword=java - Legacy keyword search (public).
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<BookDTO>>> searchBooks(
            @RequestParam String keyword) {
        List<BookDTO> results = bookService.searchBooks(keyword);
        return ResponseEntity.ok(ApiResponse.success(results));
    }

    /**
     * GET /api/v1/books/search/advanced - Multi-criteria + paginated + sorted search.
     * Example: /api/v1/books/search/advanced?title=java&categoryId=3&availableOnly=true&page=0&size=10&sort=title,asc
     */
    @GetMapping("/search/advanced")
    public ResponseEntity<ApiResponse<PagedResponse<BookDTO>>> advancedSearch(
            @ModelAttribute BookSearchCriteria criteria,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(bookService.searchBooks(criteria, pageable)));
    }

    /**
     * GET /api/v1/books/available - Get books with available copies (public)
     */
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<BookDTO>>> getAvailableBooks() {
        List<BookDTO> books = bookService.getAvailableBooks();
        return ResponseEntity.ok(ApiResponse.success(books));
    }

    /**
     * GET /api/v1/books/category/{categoryId} - Get books by category
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<BookDTO>>> getBooksByCategory(
            @PathVariable Long categoryId) {
        List<BookDTO> books = bookService.getBooksByCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(books));
    }
}
