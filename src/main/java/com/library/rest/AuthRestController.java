package com.library.rest;

import com.library.dto.ApiResponse;
import com.library.dto.JwtResponse;
import com.library.dto.LoginRequest;
import com.library.dto.RegisterRequest;
import com.library.dto.UserDTO;
import com.library.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * AuthRestController - REST API for authentication (JWT based)
 * 
 * Endpoints:
 * - POST /api/v1/auth/register - Public signup
 * - POST /api/v1/auth/login - Public login, returns JWT
 * - GET /api/v1/auth/me - Protected, returns current user (needs Bearer token)
 * 
 * Test with Postman:
 * POST http://localhost:8080/api/v1/auth/login
 * Body: {"username":"admin","password":"Admin@123"}
 * Response: {"success":true,"data":{"token":"eyJ...","role":"ROLE_ADMIN"}}
 * Then: GET http://localhost:8080/api/v1/books with Header Authorization: Bearer eyJ...
 */
@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthRestController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDTO>> register(@Valid @RequestBody RegisterRequest request) {
        UserDTO user = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(user, "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@Valid @RequestBody LoginRequest request) {
        JwtResponse jwt = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(jwt, "Login successful"));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDTO>> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Not authenticated"));
        }
        UserDTO user = authService.getUserByUsername(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(user));
    }
}
