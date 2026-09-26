package com.library.service.impl;

import com.library.dto.BorrowDTO;
import com.library.entity.Book;
import com.library.entity.BorrowRecord;
import com.library.entity.Member;
import com.library.exception.ResourceNotFoundException;
import com.library.mapper.BorrowMapper;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRecordRepository;
import com.library.repository.MemberRepository;
import com.library.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * BorrowServiceImpl - Business logic for book borrowing operations
 * 
 * This service demonstrates:
 * - Transaction management (@Transactional)
 * - Business rule enforcement (check availability before borrowing)
 * - Entity relationship management (Book <-> Member via BorrowRecord)
 * - Core Java date API (LocalDate) for due date calculations
 */
@Service
@Transactional
public class BorrowServiceImpl implements BorrowService {

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BorrowMapper borrowMapper;

    @Override
    public BorrowDTO borrowBook(Long bookId, Long memberId) {
        // 1. Find the book
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", bookId));

        // 2. Check availability (business rule)
        if (!book.isAvailable()) {
            throw new RuntimeException("Book is not available for borrowing. No copies left.");
        }

        // 3. Find the member
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member", memberId));

        // 4. Create borrow record
        BorrowRecord record = new BorrowRecord(book, member);
        record.setBorrowDate(LocalDate.now());
        record.setDueDate(LocalDate.now().plusDays(14)); // 2 weeks borrowing period
        record.setStatus("BORROWED");

        // 5. Decrease available copies (Core Java: modifying object state)
        book.decreaseAvailableCopies();
        bookRepository.save(book);

        // 6. Save borrow record
        BorrowRecord saved = borrowRecordRepository.save(record);
        return borrowMapper.toDTO(saved);
    }

    @Override
    public BorrowDTO returnBook(Long borrowId) {
        // 1. Find borrow record
        BorrowRecord record = borrowRecordRepository.findById(borrowId)
                .orElseThrow(() -> new ResourceNotFoundException("BorrowRecord", borrowId));

        // 2. Check if already returned
        if ("RETURNED".equals(record.getStatus())) {
            throw new RuntimeException("Book has already been returned.");
        }

        // 3. Return the book (updates status, return date, and calculates fine)
        record.returnBook();

        // 4. Increase available copies
        Book book = record.getBook();
        book.increaseAvailableCopies();
        bookRepository.save(book);

        // 5. Save updated record
        BorrowRecord saved = borrowRecordRepository.save(record);
        return borrowMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowDTO> getAllBorrows() {
        List<BorrowRecord> records = borrowRecordRepository.findAll();
        return borrowMapper.toDTOList(records);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowDTO> getBorrowsByMember(Long memberId) {
        List<BorrowRecord> records = borrowRecordRepository.findByMemberMemberId(memberId);
        return borrowMapper.toDTOList(records);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowDTO> getOverdueBorrows() {
        List<BorrowRecord> records = borrowRecordRepository.findOverdueRecords(LocalDate.now());
        return borrowMapper.toDTOList(records);
    }

    @Override
    @Transactional(readOnly = true)
    public long getActiveBorrowCount() {
        return borrowRecordRepository.countByStatus("BORROWED");
    }
}
