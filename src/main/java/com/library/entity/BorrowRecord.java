package com.library.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * BorrowRecord Entity - Tracks book borrowing transactions
 * 
 * Demonstrates:
 * - @ManyToOne relationships (one record belongs to one book and one member)
 * - Status enum-like field (BORROWED, RETURNED, OVERDUE)
 * - Business logic in entity (calculateFine, calculateDaysOverdue)
 * - Core Java date/time API usage (LocalDate, ChronoUnit)
 */
@Entity
@Table(name = "borrow_records")
public class BorrowRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "borrow_id")
    private Long borrowId;

    // Many-to-One: Many borrow records can reference one book
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    // Many-to-One: Many borrow records can belong to one member
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "borrow_date", nullable = false)
    private LocalDate borrowDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(nullable = false, length = 20)
    private String status = "BORROWED"; // BORROWED, RETURNED, OVERDUE

    @Column(name = "fine_amount", precision = 10, scale = 2)
    private BigDecimal fineAmount = BigDecimal.ZERO;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Default constructor
    public BorrowRecord() {
        this.createdAt = LocalDateTime.now();
        this.borrowDate = LocalDate.now();
        this.dueDate = LocalDate.now().plusDays(14); // 2 weeks borrowing period
    }

    // Constructor for quick creation
    public BorrowRecord(Book book, Member member) {
        this();
        this.book = book;
        this.member = member;
    }

    // ==================== Business Logic Methods (Core Java) ====================

    /**
     * Check if this borrow record is overdue
     * Uses Core Java LocalDate API and ChronoUnit for date calculations
     */
    public boolean isOverdue() {
        if ("RETURNED".equals(status)) {
            return false;
        }
        return LocalDate.now().isAfter(dueDate);
    }

    /**
     * Calculate the number of days overdue
     * Demonstrates Core Java date/time API usage
     */
    public long calculateDaysOverdue() {
        if (!isOverdue()) {
            return 0;
        }
        // ChronoUnit.DAYS.between() calculates days between two dates
        return ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }

    /**
     * Calculate the fine amount based on overdue days
     * Fine rate: $0.50 per day overdue
     * Demonstrates Core Java BigDecimal for precise monetary calculations
     */
    public BigDecimal calculateFine() {
        long daysOverdue = calculateDaysOverdue();
        // BigDecimal is used for precise decimal arithmetic (best practice for money)
        BigDecimal fineRatePerDay = new BigDecimal("0.50");
        return fineRatePerDay.multiply(new BigDecimal(daysOverdue));
    }

    /**
     * Return the book - update status and calculate fine
     * Demonstrates Core Java LocalDate API
     */
    public void returnBook() {
        this.returnDate = LocalDate.now();
        this.status = "RETURNED";
        this.fineAmount = calculateFine();
    }

    // ==================== Getters and Setters ====================
    public Long getBorrowId() { return borrowId; }
    public void setBorrowId(Long borrowId) { this.borrowId = borrowId; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }

    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }

    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getFineAmount() { return fineAmount; }
    public void setFineAmount(BigDecimal fineAmount) { this.fineAmount = fineAmount; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return "BorrowRecord{borrowId=" + borrowId + ", book=" + (book != null ? book.getTitle() : "null") +
               ", member=" + (member != null ? member.getFullName() : "null") +
               ", status='" + status + "'}";
    }
}
