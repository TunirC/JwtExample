package com.security.jwt.security;

import com.security.jwt.exceptions.TokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtHelper jwtHelper;
    private UserDetailsService userDetailsService;

    @Autowired
    JwtAuthenticationFilter(JwtHelper jwtHelper, UserDetailsService userDetailsService) {
        this.jwtHelper = jwtHelper;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestHeader = request.getHeader("Authorization");
        log.info("Request header : {}", requestHeader);
        String token = null, username = null;

        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
            try {
                token = requestHeader.substring(7);
                username = jwtHelper.getUserNameFromToken(token);
            } catch (ExpiredJwtException expiredJwtException) {
                throw new TokenException("Failed for [" + token + "] . Refresh Token was expired.", expiredJwtException, request);
            } catch (MalformedJwtException malformedJwtException) {
                throw new TokenException("Failed for [" + token + "] . Malformed Token found.", malformedJwtException, request);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            log.info("Invalid header value!");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtHelper.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                log.error("user name validation failed!");
            }
        }

        filterChain.doFilter(request, response);
    }
}
