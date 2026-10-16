package com.library.service.impl;

import com.library.dto.BorrowDTO;
import com.library.entity.Book;
import com.library.entity.BorrowRecord;
import com.library.entity.Member;
import com.library.exception.BookNotAvailableException;
import com.library.exception.ResourceNotFoundException;
import com.library.mapper.BorrowMapper;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRecordRepository;
import com.library.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for BorrowServiceImpl - borrowing, returning, fines.
 * Repositories are mocked; the real BorrowMapper converts entities to DTOs.
 */
@ExtendWith(MockitoExtension.class)
class BorrowServiceImplTest {

    @Mock
    private BorrowRecordRepository borrowRecordRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private MemberRepository memberRepository;

    @Spy
    private BorrowMapper borrowMapper = new BorrowMapper();

    @InjectMocks
    private BorrowServiceImpl borrowService;

    private Book book;
    private Member member;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setBookId(1L);
        book.setTitle("Effective Java");
        book.setTotalCopies(2);
        book.setAvailableCopies(2);

        member = new Member();
        member.setMemberId(1L);
        member.setFirstName("Alice");
        member.setLastName("Johnson");
        member.setEmail("alice@email.com");
    }

    @Test
    void borrowBook_sets14DayDueDate_andDecrementsCopies() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(borrowRecordRepository.save(any(BorrowRecord.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        BorrowDTO dto = borrowService.borrowBook(1L, 1L);

        assertThat(dto.getDueDate()).isEqualTo(LocalDate.now().plusDays(14));
        assertThat(dto.getStatus()).isEqualTo("BORROWED");
        assertThat(book.getAvailableCopies()).isEqualTo(1);
        verify(bookRepository).save(book);
    }

    @Test
    void borrowBook_withNoCopies_throws409() {
        book.setAvailableCopies(0);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertThatThrownBy(() -> borrowService.borrowBook(1L, 1L))
                .isInstanceOf(BookNotAvailableException.class);
    }

    @Test
    void borrowBook_unknownBook_throws404() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> borrowService.borrowBook(99L, 1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void returnBook_onTime_hasZeroFine_andRestoresCopy() {
        BorrowRecord record = new BorrowRecord(book, member);
        record.setBorrowDate(LocalDate.now().minusDays(3));
        record.setDueDate(LocalDate.now().plusDays(11));
        record.setStatus("BORROWED");
        book.setAvailableCopies(1);

        when(borrowRecordRepository.findById(10L)).thenReturn(Optional.of(record));
        when(borrowRecordRepository.save(any(BorrowRecord.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        BorrowDTO dto = borrowService.returnBook(10L);

        assertThat(dto.getStatus()).isEqualTo("RETURNED");
        assertThat(dto.getFineAmount()).isZero();
        assertThat(book.getAvailableCopies()).isEqualTo(2);
    }

    @Test
    void returnBook_overdue_calculatesFine() {
        BorrowRecord record = new BorrowRecord(book, member);
        record.setBorrowDate(LocalDate.now().minusDays(20));
        record.setDueDate(LocalDate.now().minusDays(6));
        record.setStatus("BORROWED");

        when(borrowRecordRepository.findById(11L)).thenReturn(Optional.of(record));
        when(borrowRecordRepository.save(any(BorrowRecord.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        BorrowDTO dto = borrowService.returnBook(11L);

        // $0.50 x 6 overdue days = $3.00
        assertThat(dto.getFineAmount()).isEqualByComparingTo("3.00");
    }

    @Test
    void returnBook_twice_throws409() {
        BorrowRecord record = new BorrowRecord(book, member);
        record.setStatus("RETURNED");
        when(borrowRecordRepository.findById(12L)).thenReturn(Optional.of(record));

        assertThatThrownBy(() -> borrowService.returnBook(12L))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void getBorrowById_unknown_throws404() {
        when(borrowRecordRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> borrowService.getBorrowById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
