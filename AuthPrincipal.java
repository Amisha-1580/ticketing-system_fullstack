package com.example.ticketing.security;

public record AuthPrincipal(Long id, String email, String role) { }
