package com.library.repository;

import com.library.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Repository tests - derived queries and the JPQL searchBooks query.
 * Runs against H2 (see src/test/resources/application.properties).
 */
@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager em;

    private Book persist(String title, String isbn, int total, int available) {
        Book b = new Book();
        b.setTitle(title);
        b.setIsbn(isbn);
        b.setTotalCopies(total);
        b.setAvailableCopies(available);
        return em.persistAndFlush(b);
    }

    @Test
    void findByTitleContainingIgnoreCase_matchesRegardlessOfCase() {
        persist("Effective Java", "978-0000000101", 2, 2);

        List<Book> found = bookRepository.findByTitleContainingIgnoreCase("effective");

        assertThat(found).hasSize(1);
        assertThat(found.get(0).getTitle()).isEqualTo("Effective Java");
    }

    @Test
    void searchBooks_matchesTitleOrIsbn() {
        persist("Clean Code", "978-0000000102", 2, 2);

        assertThat(bookRepository.searchBooks("Clean")).hasSize(1);
        assertThat(bookRepository.searchBooks("978-0000000102")).hasSize(1);
        assertThat(bookRepository.searchBooks("no-such-book-xyz")).isEmpty();
    }

    @Test
    void findByAvailableCopiesGreaterThan_onlyReturnsInStock() {
        persist("In Stock", "978-0000000103", 2, 2);
        persist("Out of Stock", "978-0000000104", 2, 0);

        List<Book> available = bookRepository.findByAvailableCopiesGreaterThan(0);

        assertThat(available).extracting(Book::getTitle).contains("In Stock");
        assertThat(available).extracting(Book::getTitle).doesNotContain("Out of Stock");
    }
}
