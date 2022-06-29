package com.dev.springboot.backend.apirest.models.dao;

import com.dev.springboot.backend.apirest.models.entities.Book;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IBookDao extends PagingAndSortingRepository<Book, Long> {
}
