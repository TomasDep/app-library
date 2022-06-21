package com.dev.springboot.backend.apirest.models.services;

import com.dev.springboot.backend.apirest.models.entities.Author;

import java.util.List;

public interface IAuthorService {
    public List<Author> findAll();
    public Author findById(Long id);
    public Author save(Author author);
    public void delete(Long id);
}
