package com.dev.springboot.backend.apirest.models.dao;

import com.dev.springboot.backend.apirest.models.entities.Author;
import org.springframework.data.repository.CrudRepository;

public interface IAuthorDao extends CrudRepository<Author, Long> {
}
