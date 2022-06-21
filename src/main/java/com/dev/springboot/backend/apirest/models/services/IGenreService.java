package com.dev.springboot.backend.apirest.models.services;

import com.dev.springboot.backend.apirest.models.entities.Genre;

import java.util.List;

public interface IGenreService {
    public List<Genre> findAll();
    public Genre findById(Long id);
    public Genre save(Genre genre);
    public void delete(Long id);
}
