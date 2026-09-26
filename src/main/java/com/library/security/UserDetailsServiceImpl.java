package com.library.security;

import com.library.entity.User;
import com.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * UserDetailsServiceImpl - Bridges our User entity to Spring Security
 * 
 * Spring Security calls loadUserByUsername() during authentication (login).
 * It expects a UserDetails object containing username, password (hashed), and authorities (roles).
 * 
 * Important: This service is used by both formLogin (JSP) and JWT (REST) flows.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Allow login via username OR email
        User user = userRepository.findByUsername(username)
                .or(() -> userRepository.findByEmail(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username/email: " + username));

        // Build Spring Security's UserDetails
        // Authorities: single role like ROLE_ADMIN -> SimpleGrantedAuthority
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true, true, true, // accountNonExpired, credentialsNonExpired, accountNonLocked
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}
