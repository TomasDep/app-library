package com.dev.springboot.backend.apirest.models.dao;

import com.dev.springboot.backend.apirest.models.entities.Author;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IAuthorDao extends PagingAndSortingRepository<Author, Long> {
}
