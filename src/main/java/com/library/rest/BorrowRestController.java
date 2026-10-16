package com.library.rest;

import com.library.dto.ApiResponse;
import com.library.dto.BorrowDTO;
import com.library.exception.ResourceNotFoundException;
import com.library.service.BorrowService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * BorrowRestController - REST API for borrowing operations.
 *
 * Authorization:
 * - Borrow/return/history: member or librarian.
 * - Full borrow list / stats: librarian only (members use /member/{id} or /overdue).
 */
@RestController
@RequestMapping("/api/v1/borrows")
@CrossOrigin(origins = "*")
public class BorrowRestController {

    @Autowired
    private BorrowService borrowService;

    /**
     * GET /api/v1/borrows - List all borrow records (librarian only)
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
    public ResponseEntity<ApiResponse<List<BorrowDTO>>> getAllBorrows() {
        List<BorrowDTO> borrows = borrowService.getAllBorrows();
        return ResponseEntity.ok(ApiResponse.success(borrows));
    }

    /**
     * GET /api/v1/borrows/{id} - Get borrow record by ID (member or librarian)
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MEMBER','USER','LIBRARIAN','ADMIN')")
    public ResponseEntity<ApiResponse<BorrowDTO>> getBorrowById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ApiResponse.success(borrowService.getBorrowById(id)));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/v1/borrows/member/{memberId} - Borrows for one member.
     */
    @GetMapping("/member/{memberId}")
    @PreAuthorize("hasAnyRole('MEMBER','USER','LIBRARIAN','ADMIN')")
    public ResponseEntity<ApiResponse<List<BorrowDTO>>> getBorrowsByMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(ApiResponse.success(borrowService.getBorrowsByMember(memberId)));
    }

    /**
     * POST /api/v1/borrows - Borrow a book (member or librarian).
     * Body: {"bookId": 1, "memberId": 1}
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('MEMBER','USER','LIBRARIAN','ADMIN')")
    public ResponseEntity<ApiResponse<BorrowDTO>> borrowBook(
            @RequestBody Map<String, @NotNull Long> request) {
        Long bookId = request.get("bookId");
        Long memberId = request.get("memberId");
        if (bookId == null || memberId == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Both bookId and memberId are required"));
        }
        BorrowDTO borrowed = borrowService.borrowBook(bookId, memberId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(borrowed, "Book borrowed successfully"));
    }

    /**
     * PUT /api/v1/borrows/{id}/return - Return a borrowed book (member or librarian)
     */
    @PutMapping("/{id}/return")
    @PreAuthorize("hasAnyRole('MEMBER','USER','LIBRARIAN','ADMIN')")
    public ResponseEntity<ApiResponse<BorrowDTO>> returnBook(@PathVariable Long id) {
        BorrowDTO returned = borrowService.returnBook(id);
        return ResponseEntity.ok(ApiResponse.success(returned, "Book returned successfully"));
    }

    /**
     * GET /api/v1/borrows/overdue - Get all overdue borrows (member or librarian)
     */
    @GetMapping("/overdue")
    @PreAuthorize("hasAnyRole('MEMBER','USER','LIBRARIAN','ADMIN')")
    public ResponseEntity<ApiResponse<List<BorrowDTO>>> getOverdueBorrows() {
        List<BorrowDTO> overdue = borrowService.getOverdueBorrows();
        return ResponseEntity.ok(ApiResponse.success(overdue));
    }

    /**
     * GET /api/v1/borrows/stats - Borrowing statistics (librarian only)
     */
    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
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
