package com.application.security;

import com.application.service.interfaces.TokenService;
import com.example.entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;


@Component
public class JwtProvider implements TokenService  {

    private final JwtProperties jwtProperties;
    private final Key secretKey;

    public JwtProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String generateAccessToken(String subject) {
        long expirationMillis = parseDuration(jwtProperties.getAccessTokenExpiration());
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateAccessToken(User user) {
        return generateAccessToken(user.getEmail());
    }


    public String generateRefreshToken(User user) {
        long expirationMillis = parseDuration(jwtProperties.getRefreshTokenExpiration());
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }


    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }


    private long parseDuration(String duration) {
        if (duration.endsWith("m")) return Long.parseLong(duration.replace("m", "")) * 60 * 1000;
        if (duration.endsWith("h")) return Long.parseLong(duration.replace("h", "")) * 60 * 60 * 1000;
        if (duration.endsWith("d")) return Long.parseLong(duration.replace("d", "")) * 24 * 60 * 60 * 1000;
        return Long.parseLong(duration);
    }

}
