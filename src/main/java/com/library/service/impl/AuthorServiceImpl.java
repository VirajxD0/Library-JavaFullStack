package com.library.service.impl;

import com.library.dto.AuthorDTO;
import com.library.entity.Author;
import com.library.exception.ResourceNotFoundException;
import com.library.mapper.AuthorMapper;
import com.library.repository.AuthorRepository;
import com.library.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * AuthorServiceImpl - Business logic for Author operations
 */
@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorMapper authorMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AuthorDTO> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        return authorMapper.toDTOList(authors);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorDTO getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", id));
        return authorMapper.toDTO(author);
    }

    @Override
    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        Author author = authorMapper.toEntity(authorDTO);
        Author saved = authorRepository.save(author);
        return authorMapper.toDTO(saved);
    }

    @Override
    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) {
        Author existing = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", id));
        if (authorDTO.getFirstName() != null) existing.setFirstName(authorDTO.getFirstName());
        if (authorDTO.getLastName() != null) existing.setLastName(authorDTO.getLastName());
        if (authorDTO.getEmail() != null) existing.setEmail(authorDTO.getEmail());
        if (authorDTO.getBiography() != null) existing.setBiography(authorDTO.getBiography());
        Author updated = authorRepository.save(existing);
        return authorMapper.toDTO(updated);
    }

    @Override
    public void deleteAuthor(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", id));
        authorRepository.delete(author);
    }
}
