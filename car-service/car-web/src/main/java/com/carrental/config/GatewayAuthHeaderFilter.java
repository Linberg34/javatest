package com.carrental.config;

import com.example.common.util.UserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Component
public class GatewayAuthHeaderFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String userId    = request.getHeader("X-User-Id");
        String userEmail = request.getHeader("X-User-Email");
        String rolesHeader = request.getHeader("X-User-Roles");

        log.debug("AuthHeaderFilter ► X-User-Id={}  X-User-Email={}  X-User-Roles={}",
                userId, userEmail, rolesHeader);

        if (userId != null && userEmail != null
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            if (rolesHeader != null && !rolesHeader.isBlank()) {
                for (String role : rolesHeader.split(",")) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                }
            } else {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }

            UserPrincipal principal = new UserPrincipal(userId, userEmail);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(principal, null, authorities);

            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);

            log.debug("User authenticated ► principal={} authorities={}",
                    auth.getName(), authorities);
        }

        chain.doFilter(request, response);
    }
}