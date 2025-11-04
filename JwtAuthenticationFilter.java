package com.example.ticketing.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    public JwtAuthenticationFilter(JwtUtils jwtUtils){ this.jwtUtils = jwtUtils; }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try{
                var claims = jwtUtils.validate(token).getBody();
                String username = claims.getSubject();
                String role = (String) claims.get("role");
                Long id = ((Number) claims.get("id")).longValue();
                var auth = new UsernamePasswordAuthenticationToken(
                        new AuthPrincipal(id, username, role),
                        null,
                        List.of(new SimpleGrantedAuthority(role))
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }catch(Exception ex){
                // invalid token - ignore and let spring handle unauthorized
            }
        }
        chain.doFilter(request, response);
    }
}
