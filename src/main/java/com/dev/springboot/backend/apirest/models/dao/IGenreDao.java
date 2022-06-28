package com.dev.springboot.backend.apirest.models.dao;

import com.dev.springboot.backend.apirest.models.entities.Genre;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IGenreDao extends PagingAndSortingRepository<Genre, Long> {
}