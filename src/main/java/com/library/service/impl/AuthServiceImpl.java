package com.library.service.impl;

import com.library.dto.JwtResponse;
import com.library.dto.LoginRequest;
import com.library.dto.RegisterRequest;
import com.library.dto.UserDTO;
import com.library.entity.Member;
import com.library.entity.Role;
import com.library.entity.User;
import com.library.exception.DuplicateResourceException;
import com.library.mapper.UserMapper;
import com.library.repository.MemberRepository;
import com.library.repository.UserRepository;
import com.library.security.JwtTokenProvider;
import com.library.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * AuthServiceImpl - Handles registration and JWT login logic
 * 
 * Flow register:
 * 1. Validate confirmPassword matches
 * 2. Check duplicate username/email
 * 3. Encode password with BCrypt
 * 4. Parse role (ROLE_USER default, only allow ADMIN if requested safely)
 * 5. Create Member profile if firstName provided
 * 6. Save User
 * 
 * Flow login:
 * 1. AuthenticationManager.authenticate(token) -> calls UserDetailsService + PasswordEncoder
 * 2. If success, generate JWT via JwtTokenProvider
 * 3. Return JwtResponse
 */
@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDTO register(RegisterRequest request) {
        // 1. Validate passwords match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // 2. Check duplicates
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists: " + request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + request.getEmail());
        }

        // 3. Resolve role - default USER, allow ADMIN only if explicitly requested (in real app, restrict to ADMIN creator)
        Role role = Role.ROLE_USER;
        if ("ROLE_ADMIN".equalsIgnoreCase(request.getRole()) || "ADMIN".equalsIgnoreCase(request.getRole())) {
            role = Role.ROLE_ADMIN;
        }

        // 4. Create User entity
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // BCrypt hash
        user.setRole(role);
        user.setEnabled(true);

        // 5. Optionally create linked Member profile (for borrowing)
        if (request.getFirstName() != null && !request.getFirstName().isBlank()) {
            Member member = new Member();
            member.setFirstName(request.getFirstName());
            member.setLastName(request.getLastName() != null ? request.getLastName() : "User");
            member.setEmail(request.getEmail());
            member.setMembershipDate(LocalDate.now());
            member.setMembershipType("STANDARD");
            member.setActive(true);
            // Save member first via cascade or explicitly
            member = memberRepository.save(member);
            user.setMember(member);
        }

        User saved = userRepository.save(user);
        return userMapper.toDTO(saved);
    }

    @Override
    public JwtResponse login(LoginRequest request) {
        // 1. Authenticate - throws BadCredentialsException if fails (handled by GlobalExceptionHandler)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // 2. Generate JWT
        String jwt = jwtTokenProvider.generateToken(authentication);

        // 3. Load user details for response
        User user = userRepository.findByUsername(authentication.getName())
                .or(() -> userRepository.findByEmail(authentication.getName()))
                .orElseThrow();

        return new JwtResponse(jwt, user.getId(), user.getUsername(), user.getEmail(), user.getRole().name());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .or(() -> userRepository.findByEmail(username))
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDTO(user);
    }
}
