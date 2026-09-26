package com.library.mapper;

import com.library.dto.AuthorDTO;
import com.library.entity.Author;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * AuthorMapper - Converts between Author entity and AuthorDTO
 * 
 * Why use Mappers?
 * - Entities are tied to the database (JPA annotations)
 * - DTOs are designed for API responses (clean, no circular references)
 * - Mapper acts as a translator between the two layers
 */
@Component
public class AuthorMapper {

    /**
     * Convert Author entity to AuthorDTO
     */
    public AuthorDTO toDTO(Author author) {
        if (author == null) return null;

        AuthorDTO dto = new AuthorDTO();
        dto.setAuthorId(author.getAuthorId());
        dto.setFirstName(author.getFirstName());
        dto.setLastName(author.getLastName());
        dto.setEmail(author.getEmail());
        dto.setBiography(author.getBiography());
        return dto;
    }

    /**
     * Convert AuthorDTO to Author entity
     */
    public Author toEntity(AuthorDTO dto) {
        if (dto == null) return null;

        Author author = new Author();
        author.setAuthorId(dto.getAuthorId());
        author.setFirstName(dto.getFirstName());
        author.setLastName(dto.getLastName());
        author.setEmail(dto.getEmail());
        author.setBiography(dto.getBiography());
        return author;
    }

    /**
     * Convert list of entities to list of DTOs
     * Core Java: Using ArrayList and for-each loop
     */
    public List<AuthorDTO> toDTOList(List<Author> authors) {
        List<AuthorDTO> dtos = new ArrayList<>();
        for (Author author : authors) {
            dtos.add(toDTO(author));
        }
        return dtos;
    }
}
