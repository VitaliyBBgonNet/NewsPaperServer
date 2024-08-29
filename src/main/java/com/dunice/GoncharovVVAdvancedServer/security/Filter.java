package com.dunice.GoncharovVVAdvancedServer.security;

import com.dunice.GoncharovVVAdvancedServer.constants.StringConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class Filter extends OncePerRequestFilter {

    private final TokenSecurity tokenSecurity;

    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String bearerToken = request.getHeader(StringConstants.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith(StringConstants.BEARER)) {
            String token = bearerToken.substring(7);
            if (tokenSecurity.isTokenValid(token)) {
                String id = tokenSecurity.getIdFromToken(token);
                CustomUserDetails currentUser = customUserDetailsService.loadUserByUsername(id);
                UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(
                        currentUser, null, currentUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(user);
            }
        }
        filterChain.doFilter(request, response);
    }
}