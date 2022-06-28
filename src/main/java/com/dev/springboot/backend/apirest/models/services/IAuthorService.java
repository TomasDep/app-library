package com.dev.springboot.backend.apirest.models.services;

import com.dev.springboot.backend.apirest.models.entities.Author;
import org.springframework.data.domain.Page;

public interface IAuthorService {
    public Page<Author> findAll(int pageNumber, int pageSize);
    public Author findById(Long id);
    public Author save(Author author);
    public void delete(Long id);
}
