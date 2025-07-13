package com.demo.store.service;

import com.demo.store.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    @Value("${spring.jwt.secret}")
    private String secret;

    public String generateAccessToken(User user){
        //final long tokenExpiration = 86400;  // seconds in a day
        final long tokenExpiration = 30;  // 30 seconds , used to test refresh token
        return generateToken(user, tokenExpiration);
    }

    public String generateRefreshToken(User user){
        final long tokenExpiration = 604800;  // 7 days
        return generateToken(user, tokenExpiration);
    }

    public String generateToken(User user, long tokenExpiration) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public boolean validateToken(String token) {
        Claims claims;
        try {
            claims = getClaims(token);
        } catch (JwtException e) {
            return false;
        }
        return claims.getExpiration().after(new Date());   //checks if expiration is after current date
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = getClaims(token);
        return Long.valueOf(claims.getSubject());
    }
}
