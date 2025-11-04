package com.example.ticketing.controller;

import com.example.ticketing.entity.Role;
import com.example.ticketing.entity.User;
import com.example.ticketing.repository.UserRepository;
import com.example.ticketing.security.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Implements registration and login with BCrypt and JWT.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepo;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtUtils jwtUtils;

    public AuthController(UserRepository userRepo, JwtUtils jwtUtils){
        this.userRepo = userRepo; this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User u){
        if(userRepo.findByEmail(u.getEmail()).isPresent()) return ResponseEntity.badRequest().body(Map.of("error","email exists"));
        u.setId(null);
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        if(u.getRole() == null) u.setRole(Role.ROLE_USER);
        User saved = userRepo.save(u);
        String token = jwtUtils.generateToken(saved.getEmail(), saved.getRole().name(), saved.getId());
        return ResponseEntity.ok(Map.of("token", token, "user", saved));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> body){
        String email = body.get("email"); String password = body.get("password");
        return userRepo.findByEmail(email)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()))
                .map(u -> {
                    String token = jwtUtils.generateToken(u.getEmail(), u.getRole().name(), u.getId());
                    return ResponseEntity.ok(Map.of("token", token, "user", u));
                })
                .orElse(ResponseEntity.status(401).body(Map.of("error","invalid credentials")));
    }
}
