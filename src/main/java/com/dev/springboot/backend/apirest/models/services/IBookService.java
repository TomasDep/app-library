package com.dev.springboot.backend.apirest.models.services;

import com.dev.springboot.backend.apirest.models.entities.Book;
import org.springframework.data.domain.Page;

public interface IBookService {
    public Page<Book> findAll(int pageNumber, int pageSize);
    public Book findById(Long id);
    public Book save(Book book);
    public void delete(Long id);
}
