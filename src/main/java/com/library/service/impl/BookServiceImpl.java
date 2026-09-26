package com.library.service.impl;

import com.library.dto.BookDTO;
import com.library.entity.Author;
import com.library.entity.Book;
import com.library.entity.Category;
import com.library.entity.Publisher;
import com.library.exception.ResourceNotFoundException;
import com.library.mapper.BookMapper;
import com.library.repository.AuthorRepository;
import com.library.repository.BookRepository;
import com.library.repository.CategoryRepository;
import com.library.repository.PublisherRepository;
import com.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * BookServiceImpl - Business logic implementation for Book operations
 * 
 * Key Annotations:
 * @Service - Marks this as a Spring service component (business logic layer)
 * @Transactional - Each method runs in a database transaction
 *   - On success: transaction is COMMITTED
 *   - On exception: transaction is ROLLED BACK
 *   - This ensures data consistency
 * 
 * Dependency Injection:
 * - All dependencies are injected via @Autowired (constructor injection recommended in production)
 * - Spring automatically creates and manages these objects (IoC Container)
 */
@Service
@Transactional // All methods in this class are transactional
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookMapper bookMapper;

    @Override
    @Transactional(readOnly = true) // Read-only optimization for queries
    public List<BookDTO> getAllBooks(int page, int size) {
        // Spring Data JPA provides pagination support
        List<Book> books = bookRepository.findAll();
        return bookMapper.toDTOList(books);
    }

    @Override
    @Transactional(readOnly = true)
    public BookDTO getBookById(Long id) {
        // findById returns Optional<Book> - handles null safety
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));
        return bookMapper.toDTO(book);
    }

    @Override
    public BookDTO createBook(BookDTO bookDTO) {
        // Convert DTO to entity
        Book book = bookMapper.toEntity(bookDTO);

        // Set available copies equal to total copies for new books
        book.setAvailableCopies(book.getTotalCopies());

        // Look up and set Category (if provided)
        if (bookDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(bookDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", bookDTO.getCategoryId()));
            book.setCategory(category);
        }

        // Look up and set Publisher (if provided)
        if (bookDTO.getPublisherId() != null) {
            Publisher publisher = publisherRepository.findById(bookDTO.getPublisherId())
                    .orElseThrow(() -> new ResourceNotFoundException("Publisher", bookDTO.getPublisherId()));
            book.setPublisher(publisher);
        }

        // Save to database
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDTO(savedBook);
    }

    @Override
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        // Check if book exists
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));

        // Update fields (only if provided)
        if (bookDTO.getTitle() != null) existingBook.setTitle(bookDTO.getTitle());
        if (bookDTO.getIsbn() != null) existingBook.setIsbn(bookDTO.getIsbn());
        if (bookDTO.getPublicationYear() != null) existingBook.setPublicationYear(bookDTO.getPublicationYear());
        if (bookDTO.getEdition() != null) existingBook.setEdition(bookDTO.getEdition());
        if (bookDTO.getDescription() != null) existingBook.setDescription(bookDTO.getDescription());
        if (bookDTO.getLanguage() != null) existingBook.setLanguage(bookDTO.getLanguage());

        // Update Category if provided
        if (bookDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(bookDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", bookDTO.getCategoryId()));
            existingBook.setCategory(category);
        }

        // Save changes
        Book updatedBook = bookRepository.save(existingBook);
        return bookMapper.toDTO(updatedBook);
    }

    @Override
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));
        bookRepository.delete(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> searchBooks(String keyword) {
        List<Book> books = bookRepository.searchBooks(keyword);
        return bookMapper.toDTOList(books);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> getAvailableBooks() {
        List<Book> books = bookRepository.findByAvailableCopiesGreaterThan(0);
        return bookMapper.toDTOList(books);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> getBooksByCategory(Long categoryId) {
        List<Book> books = bookRepository.findByCategoryCategoryId(categoryId);
        return bookMapper.toDTOList(books);
    }
}
