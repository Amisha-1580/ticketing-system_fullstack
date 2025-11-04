package com.example.ticketing.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    @Value("${jwt.secret}") 
    private String jwtSecret;
    @Value("${jwt.expirationMs}") 
    private long jwtExpirationMs;

    private Key key() { return Keys.hmacShaKeyFor(jwtSecret.getBytes()); }

    public String generateToken(String username, String role, Long id){
        Date now = new Date();
        Date exp = new Date(now.getTime() + jwtExpirationMs);
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .claim("id", id)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> validate(String token){
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token);
    }
}
