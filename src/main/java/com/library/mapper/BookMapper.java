package com.library.mapper;

import com.library.dto.BookDTO;
import com.library.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * BookMapper - Converts between Book entity and BookDTO
 * 
 * Demonstrates:
 * - Dependency Injection (@Autowired) for using other mappers
 * - Entity-to-DTO conversion with relationships flattened
 * - Avoiding circular references (Book -> Author -> Book)
 */
@Component
public class BookMapper {

    @Autowired
    private AuthorMapper authorMapper;

    /**
     * Convert Book entity to BookDTO
     * Flattens nested relationships (Category, Publisher, Authors) into simple fields
     */
    public BookDTO toDTO(Book book) {
        if (book == null) return null;

        BookDTO dto = new BookDTO();
        dto.setBookId(book.getBookId());
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getIsbn());
        dto.setPublicationYear(book.getPublicationYear());
        dto.setEdition(book.getEdition());
        dto.setTotalCopies(book.getTotalCopies());
        dto.setAvailableCopies(book.getAvailableCopies());
        dto.setDescription(book.getDescription());
        dto.setLanguage(book.getLanguage());

        // Flatten Category relationship
        if (book.getCategory() != null) {
            dto.setCategoryId(book.getCategory().getCategoryId());
            dto.setCategoryName(book.getCategory().getName());
        }

        // Flatten Publisher relationship
        if (book.getPublisher() != null) {
            dto.setPublisherId(book.getPublisher().getPublisherId());
            dto.setPublisherName(book.getPublisher().getName());
        }

        // Convert Set<Author> to List<AuthorDTO>
        if (book.getAuthors() != null) {
            dto.setAuthors(authorMapper.toDTOList(new ArrayList<>(book.getAuthors())));
        }

        return dto;
    }

    /**
     * Convert BookDTO to Book entity
     * Note: Category and Publisher are NOT set here - they're set in the Service layer
     * because we need to look them up from the database
     */
    public Book toEntity(BookDTO dto) {
        if (dto == null) return null;

        Book book = new Book();
        book.setBookId(dto.getBookId());
        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setPublicationYear(dto.getPublicationYear());
        book.setEdition(dto.getEdition());
        book.setTotalCopies(dto.getTotalCopies());
        book.setAvailableCopies(dto.getAvailableCopies());
        book.setDescription(dto.getDescription());
        book.setLanguage(dto.getLanguage());
        return book;
    }

    /**
     * Convert list of entities to list of DTOs
     */
    public List<BookDTO> toDTOList(List<Book> books) {
        List<BookDTO> dtos = new ArrayList<>();
        for (Book book : books) {
            dtos.add(toDTO(book));
        }
        return dtos;
    }
}
