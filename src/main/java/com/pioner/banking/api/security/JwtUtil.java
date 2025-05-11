package com.pioner.banking.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String secretKeyProp;

    @Value("${jwt.expiration-ms}")
    private long expirationTime;

    private Key signingKey;

    @PostConstruct
    public void init() {
        byte[] keyBytes = secretKeyProp.getBytes();
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("JWT secret key must be at least 32 bytes for HS256");
        }
        signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Long userId) {
        Date nowTime = new Date();
        return Jwts.builder()
                .claim("userId", userId)
                .setIssuedAt(nowTime)
                .setExpiration(new Date(nowTime.getTime() + expirationTime))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractClaim(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long extractUserId(String token) {
        return (extractClaim(token)).get("userId", Long.class);
    }

    public Boolean validateToken(String token) {
        try {
            Claims claims = extractClaim(token);
            return !claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Error to validate JWT", e);
            return false;
        }
    }

}
