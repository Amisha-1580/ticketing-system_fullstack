package com.example.ticketing.service;

import com.example.ticketing.entity.Comment;
import com.example.ticketing.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository repo;
    public CommentService(CommentRepository repo){ this.repo = repo; }

    public Comment add(Comment c){
        c.setCreatedAt(Instant.now());
        return repo.save(c);
    }

    public List<Comment> listForTicket(Long ticketId){ return repo.findByTicketIdOrderByCreatedAtAsc(ticketId); }
}
