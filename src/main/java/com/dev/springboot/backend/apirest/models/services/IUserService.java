package com.dev.springboot.backend.apirest.models.services;

import com.dev.springboot.backend.apirest.models.entities.User;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface IUserService {
    public Page<User> findAll(int pageNumber, int pageSize);
    public User findById(Long id);
    public User save(User user);
    public void delete(Long id);
    public Optional<User> getByUsername(String username);
    public boolean existsByUsername(String username);
    public boolean existsByEmail(String email);
}