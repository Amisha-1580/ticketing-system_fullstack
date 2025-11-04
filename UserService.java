package com.example.ticketing.service;

import com.example.ticketing.entity.User;
import com.example.ticketing.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repo;
    public UserService(UserRepository repo){ this.repo = repo; }
    public List<User> listAll(){ return repo.findAll(); }
    public Optional<User> findById(Long id){ return repo.findById(id); }
    public User save(User u){ return repo.save(u); }
    public void delete(Long id){ repo.deleteById(id); }
}
