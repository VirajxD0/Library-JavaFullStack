package com.library.repository;

import com.library.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * BorrowRecord Repository - Spring Data JPA Repository for BorrowRecord entity
 * 
 * Demonstrates:
 * - Complex JPQL queries with JOINs
 * - Date-based queries using LocalDate
 * - Aggregation queries for statistics
 */
@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    /**
     * Find all borrow records for a specific member
     */
    List<BorrowRecord> findByMemberMemberId(Long memberId);

    /**
     * Find borrow records by status (BORROWED, RETURNED, OVERDUE)
     */
    List<BorrowRecord> findByStatus(String status);

    /**
     * Find overdue records - books that are borrowed and past due date
     * JPQL query with date comparison
     */
    @Query("SELECT br FROM BorrowRecord br WHERE br.status = 'BORROWED' AND br.dueDate < :currentDate")
    List<BorrowRecord> findOverdueRecords(@Param("currentDate") LocalDate currentDate);

    /**
     * Find currently borrowed books for a member (not yet returned)
     */
    @Query("SELECT br FROM BorrowRecord br WHERE br.member.memberId = :memberId AND br.status = 'BORROWED'")
    List<BorrowRecord> findActiveBorrowsByMember(@Param("memberId") Long memberId);

    /**
     * Count total active borrows
     */
    long countByStatus(String status);
}
