package com.example.ticketing.controller;

import com.example.ticketing.entity.User;
import com.example.ticketing.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Admin endpoints (no auth enforced in scaffold).
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserService userService;
    public AdminController(UserService userService){ this.userService = userService; }

    @GetMapping("/users")
    public ResponseEntity<List<User>> listUsers(){ return ResponseEntity.ok(userService.listAll()); }

    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody User u){ return ResponseEntity.ok(userService.save(u)); }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){ userService.delete(id); return ResponseEntity.ok().build(); }
}
