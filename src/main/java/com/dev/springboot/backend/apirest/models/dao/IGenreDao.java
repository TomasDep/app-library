package com.dev.springboot.backend.apirest.models.dao;

import com.dev.springboot.backend.apirest.models.entities.Genre;
import org.springframework.data.repository.CrudRepository;

public interface IGenreDao extends CrudRepository<Genre, Long> {
}