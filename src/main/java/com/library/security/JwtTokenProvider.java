package com.library.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JwtTokenProvider - Core JWT logic (generation, validation, extraction)
 * 
 * Interview Q: How does JWT work?
 * 1. After login, server creates JWT: header.payload.signature (signed with secret)
 * 2. Client stores token, sends in header: Authorization: Bearer <token>
 * 3. Server validates signature + expiry on each request via Filter
 * 
 * Why JJWT 0.12.x? Modern API using builder/parser, supports HS256.
 * Secret must be 256-bit (32 bytes) base64 encoded for HS256.
 */
@Component
public class JwtTokenProvider {

    // Injected from application.properties: jwt.secret (base64), jwt.expiration (ms)
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    /**
     * Get signing key from base64 secret
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generate JWT from Authentication (contains username + authorities)
     */
    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return generateTokenFromUsername(userPrincipal.getUsername());
    }

    /**
     * Generate token directly from username (used for manual generation)
     */
    public String generateTokenFromUsername(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(username) // sub claim
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey()) // HS256 automatically inferred from key length
                .compact();
    }

    /**
     * Extract username (subject) from token
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Validate token signature and expiry
     * Returns true if valid, false otherwise (logs reason)
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException ex) {
            System.err.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            System.err.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            System.err.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            System.err.println("JWT claims string is empty");
        } catch (JwtException ex) {
            System.err.println("JWT validation failed: " + ex.getMessage());
        }
        return false;
    }
}
