package com.dev.springboot.backend.apirest.models.services;

import com.dev.springboot.backend.apirest.models.entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    public List<User> findAll();
    public User findById(Long id);
    public User save(User user);
    public void delete(Long id);
    public Optional<User> getByUsername(String username);
    public boolean existsByUsername(String username);
    public boolean existsByEmail(String email);
}