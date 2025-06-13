package com.application.security;

import com.application.service.interfaces.TokenService;
import com.example.common.security.JwtProperties;
import com.example.entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtProvider implements TokenService {

    private final JwtProperties jwtProperties;
    private final Key secretKey;

    public JwtProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateAccessToken(User user) {
        long expirationMillis = parseDuration(jwtProperties.getAccessTokenExpiration());
        return Jwts.builder()
                // ставим в subject неизменяемый UUID
                .setSubject(user.getId().toString())
                // кладём email в отдельный клейм, если нужно где-то читать его
                .claim("email", user.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateRefreshToken(User user) {
        long expirationMillis = parseDuration(jwtProperties.getRefreshTokenExpiration());
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("email", user.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("email", String.class);
    }

    @Override
    public UUID getUserIdFromToken(String token) {
        String subject = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return UUID.fromString(subject);
    }

    @Override
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
        if (duration.endsWith("m")) return Long.parseLong(duration.replace("m", "")) * 60_000;
        if (duration.endsWith("h")) return Long.parseLong(duration.replace("h", "")) * 3_600_000;
        if (duration.endsWith("d")) return Long.parseLong(duration.replace("d", "")) * 86_400_000;
        return Long.parseLong(duration);
    }
}