package com.example.ticketing.controller;

import com.example.ticketing.entity.Comment;
import com.example.ticketing.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;
    public CommentController(CommentService commentService){ this.commentService = commentService; }

    @PostMapping
    public ResponseEntity<Comment> add(@RequestBody Comment c){
        return ResponseEntity.ok(commentService.add(c));
    }

    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<List<Comment>> list(@PathVariable Long ticketId){
        return ResponseEntity.ok(commentService.listForTicket(ticketId));
    }
}
