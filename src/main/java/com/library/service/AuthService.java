package com.library.service;

import com.library.dto.JwtResponse;
import com.library.dto.LoginRequest;
import com.library.dto.RegisterRequest;
import com.library.dto.UserDTO;

/**
 * AuthService - Business logic for authentication
 * Used by both MVC (JSP session) and REST (JWT) controllers
 */
public interface AuthService {

    /**
     * Register new user - creates User + linked Member profile
     */
    UserDTO register(RegisterRequest request);

    /**
     * Authenticate user and generate JWT (for REST)
     */
    JwtResponse login(LoginRequest request);

    /**
     * Get current user DTO by username
     */
    UserDTO getUserByUsername(String username);
}
