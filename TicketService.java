package com.example.ticketing.service;

import com.example.ticketing.entity.Ticket;
import com.example.ticketing.entity.User;
import com.example.ticketing.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    private final TicketRepository repo;
    private final EmailService emailService;
    public TicketService(TicketRepository repo, EmailService emailService){ this.repo = repo; this.emailService = emailService; }

    public Ticket create(Ticket t){
        t.setStatus(t.getStatus() == null ? com.example.ticketing.entity.Status.OPEN : t.getStatus());
        t.setCreatedAt(Instant.now());
        t.setUpdatedAt(Instant.now());
        Ticket saved = repo.save(t);
        if(saved.getAssignee() != null && saved.getAssignee().getEmail() != null){
            emailService.sendSimple(saved.getAssignee().getEmail(), "Ticket assigned to you: " + saved.getId(),
                    "Ticket " + saved.getId() + " has been assigned to you. Subject: " + saved.getSubject());
        }
        return saved;
    }
    public List<Ticket> listByOwner(Long ownerId){ return repo.findByOwnerId(ownerId); }
    public Optional<Ticket> findById(Long id){ return repo.findById(id); }
    public Ticket save(Ticket t){ t.setUpdatedAt(Instant.now()); Ticket saved = repo.save(t); return saved; }
    public List<Ticket> findAll(){ return repo.findAll(); }

    public Ticket assignTo(Long ticketId, User assignee){
        var ot = repo.findById(ticketId);
        if(ot.isEmpty()) throw new RuntimeException("ticket not found");
        Ticket t = ot.get();
        t.setAssignee(assignee);
        t.setUpdatedAt(Instant.now());
        Ticket saved = repo.save(t);
        if(assignee.getEmail() != null) {
            emailService.sendSimple(assignee.getEmail(), "Ticket assigned: " + saved.getId(),
                    "You have been assigned ticket " + saved.getId() + ". Subject: " + saved.getSubject());
        }
        return saved;
    }
}
