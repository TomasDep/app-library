package com.dev.springboot.backend.apirest.models.services;

import com.dev.springboot.backend.apirest.models.entities.Genre;
import org.springframework.data.domain.Page;

public interface IGenreService {
    public Page<Genre> findAll(int pageNumber, int pageSize);
    public Genre findById(Long id);
    public Genre save(Genre genre);
    public void delete(Long id);
}
