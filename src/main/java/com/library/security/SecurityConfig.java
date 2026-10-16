package com.library.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfig - Central Spring Security configuration.
 *
 * Two filter chains:
 * 1) API chain (stateless, JWT) for /api/**
 * 2) Web chain (stateful, formLogin + session) for JSP
 *
 * Role model (librarian vs member; legacy aliases still accepted):
 * - Librarian: ROLE_LIBRARIAN (+ legacy ROLE_ADMIN) - manages books/members/categories
 * - Member: ROLE_MEMBER (+ legacy ROLE_USER) - browses, borrows, returns
 *
 * Method-level rules with @PreAuthorize on REST controllers are the
 * primary guard; URL matchers below are defence-in-depth.
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // strength 10, salted
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/**")
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/books/search", "/api/v1/books/available").permitAll()
                // Librarian-only writes (method-level @PreAuthorize enforces the same)
                .requestMatchers(HttpMethod.POST, "/api/v1/books/**", "/api/v1/members/**", "/api/v1/categories/**").hasAnyRole("LIBRARIAN", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/books/**", "/api/v1/members/**", "/api/v1/categories/**").hasAnyRole("LIBRARIAN", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/books/**", "/api/v1/members/**", "/api/v1/categories/**").hasAnyRole("LIBRARIAN", "ADMIN")
                .requestMatchers("/api/v1/books/**", "/api/v1/members/**", "/api/v1/borrows/**", "/api/v1/categories/**").authenticated()
                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, authEx) -> {
                    res.setStatus(401);
                    res.setContentType("application/json");
                    res.getWriter().write("{\"success\":false,\"message\":\"Unauthorized - Invalid or missing JWT token\"}");
                })
            )
            .headers(headers -> headers.frameOptions(frame -> frame.disable()));
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            // DEMO: CSRF disabled for simplicity (forms work without token). In production, enable CSRF with _csrf hidden field!
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/register", "/css/**", "/js/**", "/h2-console/**", "/api/v1/auth/**", "/403", "/error/**",
                        "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/actuator/health", "/actuator/info").permitAll()
                .requestMatchers("/books/add", "/books/edit/**", "/books/delete/**", "/books/save", "/books/update/**").hasAnyRole("LIBRARIAN", "ADMIN")
                .requestMatchers("/members/**").hasAnyRole("LIBRARIAN", "ADMIN")
                .requestMatchers("/dashboard", "/books", "/books/view/**", "/books/search", "/borrows", "/borrows/borrow", "/borrows/return/**", "/borrows/overdue").hasAnyRole("MEMBER", "USER", "LIBRARIAN", "ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            )
            .exceptionHandling(ex -> ex.accessDeniedPage("/403"))
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .authenticationProvider(authenticationProvider());
        return http.build();
    }
}
