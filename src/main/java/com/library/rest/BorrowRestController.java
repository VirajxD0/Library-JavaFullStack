package com.library.rest;

import com.library.dto.ApiResponse;
import com.library.dto.BorrowDTO;
import com.library.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * BorrowRestController - REST API for borrowing operations
 * 
 * Demonstrates:
 * - Custom action endpoints (not just CRUD)
 * - @RequestBody with Map for simple request bodies
 * - Business logic endpoints (borrow, return)
 */
@RestController
@RequestMapping("/api/v1/borrows")
@CrossOrigin(origins = "*")
public class BorrowRestController {

    @Autowired
    private BorrowService borrowService;

    /**
     * GET /api/v1/borrows - List all borrow records
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<BorrowDTO>>> getAllBorrows() {
        List<BorrowDTO> borrows = borrowService.getAllBorrows();
        return ResponseEntity.ok(ApiResponse.success(borrows));
    }

    /**
     * GET /api/v1/borrows/{id} - Get borrow record by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BorrowDTO>> getBorrowById(@PathVariable Long id) {
        // For simplicity, search through all borrows
        List<BorrowDTO> allBorrows = borrowService.getAllBorrows();
        BorrowDTO found = allBorrows.stream()
                .filter(b -> b.getBorrowId().equals(id))
                .findFirst()
                .orElse(null);
        if (found == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ApiResponse.success(found));
    }

    /**
     * POST /api/v1/borrows - Borrow a book
     * 
     * Request Body:
     * {
     *   "bookId": 1,
     *   "memberId": 1
     * }
     */
    @PostMapping
    public ResponseEntity<ApiResponse<BorrowDTO>> borrowBook(
            @RequestBody Map<String, Long> request) {
        Long bookId = request.get("bookId");
        Long memberId = request.get("memberId");
        BorrowDTO borrowed = borrowService.borrowBook(bookId, memberId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(borrowed, "Book borrowed successfully"));
    }

    /**
     * PUT /api/v1/borrows/{id}/return - Return a borrowed book
     */
    @PutMapping("/{id}/return")
    public ResponseEntity<ApiResponse<BorrowDTO>> returnBook(@PathVariable Long id) {
        BorrowDTO returned = borrowService.returnBook(id);
        return ResponseEntity.ok(ApiResponse.success(returned, "Book returned successfully"));
    }

    /**
     * GET /api/v1/borrows/overdue - Get all overdue borrows
     */
    @GetMapping("/overdue")
    public ResponseEntity<ApiResponse<List<BorrowDTO>>> getOverdueBorrows() {
        List<BorrowDTO> overdue = borrowService.getOverdueBorrows();
        return ResponseEntity.ok(ApiResponse.success(overdue));
    }

    /**
     * GET /api/v1/borrows/stats - Get borrowing statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() {
        long activeCount = borrowService.getActiveBorrowCount();
        long overdueCount = borrowService.getOverdueBorrows().size();
        Map<String, Object> stats = Map.of(
                "activeBorrows", activeCount,
                "overdueBorrows", overdueCount
        );
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}
