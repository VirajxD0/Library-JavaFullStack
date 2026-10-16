package com.library.service;

import com.library.dto.BorrowDTO;
import java.util.List;

/**
 * BorrowService Interface - Business operations for book borrowing
 */
public interface BorrowService {

    /**
     * Borrow a book
     * @param bookId book to borrow
     * @param memberId member who borrows
     * @return created BorrowDTO
     */
    BorrowDTO borrowBook(Long bookId, Long memberId);

    /**
     * Return a borrowed book
     * @param borrowId borrow record ID
     * @return updated BorrowDTO
     */
    BorrowDTO returnBook(Long borrowId);

    BorrowDTO getBorrowById(Long borrowId);

    List<BorrowDTO> getAllBorrows();
    List<BorrowDTO> getBorrowsByMember(Long memberId);
    List<BorrowDTO> getOverdueBorrows();
    long getActiveBorrowCount();
}
