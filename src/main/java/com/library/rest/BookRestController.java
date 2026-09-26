package com.library.rest;

import com.library.dto.ApiResponse;
import com.library.dto.BookDTO;
import com.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * BookRestController - REST API for Book operations
 * 
 * Key Annotations:
 * @RestController = @Controller + @ResponseBody
 *   - @Controller: Marks as a Spring MVC controller
 *   - @ResponseBody: Automatically serializes return values to JSON
 * 
 * @RequestMapping("/api/v1/books"): Base URL for all endpoints in this controller
 * @CrossOrigin: Enables CORS (Cross-Origin Resource Sharing) for frontend apps
 * 
 * This controller handles HTTP requests and returns JSON responses.
 * It delegates business logic to BookService (separation of concerns).
 */
@RestController
@RequestMapping("/api/v1/books")
@CrossOrigin(origins = "*") // Allow requests from any origin (for development)
public class BookRestController {

    @Autowired
    private BookService bookService;

    /**
     * GET /api/v1/books - List all books
     * 
     * @RequestParam: Query parameters from the URL
     *   Example: /api/v1/books?page=0&size=10
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<BookDTO>>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        List<BookDTO> books = bookService.getAllBooks(page, size);
        return ResponseEntity.ok(ApiResponse.success(books));
    }

    /**
     * GET /api/v1/books/{id} - Get a single book by ID
     * 
     * @PathVariable: Extracts value from the URL path
     *   Example: /api/v1/books/5 -> id = 5
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookDTO>> getBookById(@PathVariable Long id) {
        BookDTO book = bookService.getBookById(id);
        return ResponseEntity.ok(ApiResponse.success(book));
    }

    /**
     * POST /api/v1/books - Create a new book
     * 
     * @RequestBody: Automatically deserializes JSON request body to BookDTO
     * @Valid: Triggers Bean Validation (checks @NotBlank, @Size, etc.)
     */
    @PostMapping
    public ResponseEntity<ApiResponse<BookDTO>> createBook(
            @Valid @RequestBody BookDTO bookDTO) {
        BookDTO created = bookService.createBook(bookDTO);
        // HTTP 201 Created is the standard status for successful resource creation
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Book created successfully"));
    }

    /**
     * PUT /api/v1/books/{id} - Update an existing book
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookDTO>> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookDTO bookDTO) {
        BookDTO updated = bookService.updateBook(id, bookDTO);
        return ResponseEntity.ok(ApiResponse.success(updated, "Book updated successfully"));
    }

    /**
     * DELETE /api/v1/books/{id} - Delete a book
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Book deleted successfully"));
    }

    /**
     * GET /api/v1/books/search?keyword=java - Search books by keyword
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<BookDTO>>> searchBooks(
            @RequestParam String keyword) {
        List<BookDTO> results = bookService.searchBooks(keyword);
        return ResponseEntity.ok(ApiResponse.success(results));
    }

    /**
     * GET /api/v1/books/available - Get books with available copies
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
