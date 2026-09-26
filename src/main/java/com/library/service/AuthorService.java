package com.library.service;

import com.library.dto.AuthorDTO;
import java.util.List;

/**
 * AuthorService Interface - Business operations for authors
 */
public interface AuthorService {

    List<AuthorDTO> getAllAuthors();
    AuthorDTO getAuthorById(Long id);
    AuthorDTO createAuthor(AuthorDTO authorDTO);
    AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO);
    void deleteAuthor(Long id);
}
