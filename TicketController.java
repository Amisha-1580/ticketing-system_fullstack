package com.example.ticketing.controller;

import com.example.ticketing.entity.Ticket;
import com.example.ticketing.entity.User;
import com.example.ticketing.service.TicketService;
import com.example.ticketing.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Minimal ticket endpoints. Authentication & RBAC are NOT enforced in this scaffold.
 * Use the AuthController and implement JWT + Spring Security to enable role-based access.
 */
@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketService ticketService;
    private final UserService userService;
    public TicketController(TicketService ticketService, UserService userService){
        this.ticketService = ticketService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket t){
        // expect owner.id to be set by client (demo). In real app set from authenticated principal.
        Ticket created = ticketService.create(t);
        return ResponseEntity.ok(created);
    }

@GetMapping
public ResponseEntity<List<Ticket>> listAll(){
    return ResponseEntity.ok(ticketService.findAll());
}

// search by query params: subject (contains), status, priority, assigneeId
@GetMapping("/search")
public ResponseEntity<List<Ticket>> search(@RequestParam(required=false) String subject,
                                           @RequestParam(required=false) String status,
                                           @RequestParam(required=false) String priority,
                                           @RequestParam(required=false) Long assigneeId){
    var list = ticketService.findAll();
    var stream = list.stream();
    if(subject != null && !subject.isEmpty()) stream = stream.filter(t -> t.getSubject()!=null && t.getSubject().toLowerCase().contains(subject.toLowerCase()));
    if(status != null && !status.isEmpty()){
        try{ var s = com.example.ticketing.entity.Status.valueOf(status); stream = stream.filter(t -> t.getStatus()==s); }catch(Exception e){}
    }
    if(priority != null && !priority.isEmpty()){
        try{ var p = com.example.ticketing.entity.Priority.valueOf(priority); stream = stream.filter(t -> t.getPriority()==p); }catch(Exception e){}
    }
    if(assigneeId != null){ stream = stream.filter(t -> t.getAssignee()!=null && t.getAssignee().getId().equals(assigneeId)); }
    return ResponseEntity.ok(stream.toList());
}

@GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Ticket>> listByOwner(@PathVariable Long ownerId){
        return ResponseEntity.ok(ticketService.listByOwner(ownerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id){
        Optional<Ticket> t = ticketService.findById(id);
        return t.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/assign/{userId}")
    public ResponseEntity<?> assign(@PathVariable Long id, @PathVariable Long userId){
        Optional<Ticket> ot = ticketService.findById(id);
        Optional<User> ou = userService.findById(userId);
        if(ot.isEmpty() || ou.isEmpty()) return ResponseEntity.notFound().build();
        Ticket t = ot.get();
        t.setAssignee(ou.get());
        ticketService.save(t);
        return ResponseEntity.ok(t);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam String status){
        Optional<Ticket> ot = ticketService.findById(id);
        if(ot.isEmpty()) return ResponseEntity.notFound().build();
        Ticket t = ot.get();
        try{
            t.setStatus(com.example.ticketing.entity.Status.valueOf(status));
        }catch(Exception ex){
            return ResponseEntity.badRequest().body("invalid status");
        }
        ticketService.save(t);
        return ResponseEntity.ok(t);
    }
}
