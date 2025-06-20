package com.application.security;

import com.application.service.interfaces.TokenService;
import com.example.common.enums.Role;
import com.example.common.security.JwtProperties;
import com.example.entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

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
        String subject = user.getId() != null
                ? user.getId().toString()
                : UUID.randomUUID().toString();

        Set<Role> roles = user.getRoles() != null
                ? user.getRoles()
                : Collections.emptySet();

        List<String> rolesList = roles.stream()
                .map(Role::name)
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(subject)
                .claim("email", user.getEmail())
                .claim("roles", roles)
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }


    @Override
    public String generateRefreshToken(User user) {
        long expirationMillis = parseDuration(jwtProperties.getRefreshTokenExpiration());
        String subject = user.getId() != null
                ? user.getId().toString()
                : UUID.randomUUID().toString();
        return Jwts.builder()
                .setSubject(subject)
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