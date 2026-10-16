package com.library.service.impl;

import com.library.dto.BookDTO;
import com.library.dto.BookSearchCriteria;
import com.library.dto.PagedResponse;
import com.library.entity.Book;
import com.library.mapper.AuthorMapper;
import com.library.mapper.BookMapper;
import com.library.repository.AuthorRepository;
import com.library.repository.BookRepository;
import com.library.repository.CategoryRepository;
import com.library.repository.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for BookServiceImpl - pagination, sorting, multi-criteria search.
 * Repositories are mocked; real mappers convert entities to DTOs.
 */
@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private PublisherRepository publisherRepository;
    @Mock
    private AuthorRepository authorRepository;

    @Spy
    private BookMapper bookMapper = new BookMapper();

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    void injectAuthorMapper() {
        ReflectionTestUtils.setField(bookMapper, "authorMapper", new AuthorMapper());
    }

    private Book book(Long id, String title) {
        Book b = new Book();
        b.setBookId(id);
        b.setTitle(title);
        b.setIsbn("978-00000000" + id);
        b.setTotalCopies(3);
        b.setAvailableCopies(3);
        return b;
    }

    @Test
    @SuppressWarnings("unchecked")
    void getAllBooks_forwardsPageable_andWrapsPage() {
        Pageable pageable = PageRequest.of(1, 2, Sort.by("title").ascending());
        var page = new PageImpl<>(List.of(book(1L, "Clean Code")), pageable, 5);
        when(bookRepository.findAll(eq(pageable))).thenReturn(page);

        PagedResponse<BookDTO> result = bookService.getAllBooks(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Clean Code");
        assertThat(result.getPage()).isEqualTo(1);
        assertThat(result.getTotalElements()).isEqualTo(5);
        assertThat(result.getTotalPages()).isEqualTo(3);
        assertThat(result.isLast()).isFalse();
    }

    @Test
    @SuppressWarnings("unchecked")
    void searchBooks_buildsSpecification_andReturnsPage() {
        BookSearchCriteria criteria = new BookSearchCriteria();
        criteria.setTitle("java");
        criteria.setAvailableOnly(true);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("title").ascending());
        var page = new PageImpl<>(List.of(book(2L, "Effective Java")), pageable, 1);
        when(bookRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        PagedResponse<BookDTO> result = bookService.searchBooks(criteria, pageable);

        ArgumentCaptor<Specification<Book>> specCaptor = ArgumentCaptor.forClass(Specification.class);
        verify(bookRepository).findAll(specCaptor.capture(), eq(pageable));
        assertThat(specCaptor.getValue()).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.isLast()).isTrue();
    }

    @Test
    void createBook_setsAvailableCopies_andSaves() {
        BookDTO dto = new BookDTO();
        dto.setTitle("New Book");
        dto.setIsbn("978-0000000099");
        dto.setTotalCopies(4);
        when(bookRepository.save(any(Book.class))).thenAnswer(inv -> inv.getArgument(0));

        BookDTO created = bookService.createBook(dto);

        assertThat(created.getAvailableCopies()).isEqualTo(4);
        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(captor.capture());
        assertThat(captor.getValue().getAvailableCopies()).isEqualTo(4);
    }
}
