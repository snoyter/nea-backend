package com.nea.backend.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUserDetailsService userDetailsService;
    private final TokenManager tokenManager;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        final String tokenHeader = request.getHeader("Authorization");
        String username = null;
        final String token;
        final String TOKEN_START = "Bearer ";
        if (tokenHeader == null || !tokenHeader.startsWith(TOKEN_START)) {
            filterChain.doFilter(request, response);
            return;
        }
        token = tokenHeader.substring(TOKEN_START.length());
        try {
            username = tokenManager.getUsernameFromToken(token);
        } catch (IllegalArgumentException e) {
            log.error("Невозможно получить имя пользователя из токена {}", token);
        } catch (ExpiredJwtException e) {
            log.error("Токен истёк {}", token);
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            boolean isValidToken = tokenManager.validateJwtToken(token, userDetails);
            if (isValidToken) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);

    }
}
