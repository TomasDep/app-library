package com.dev.springboot.backend.apirest.models.services.impl;

import com.dev.springboot.backend.apirest.models.dao.IGenreDao;
import com.dev.springboot.backend.apirest.models.entities.Genre;
import com.dev.springboot.backend.apirest.models.services.IGenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreServiceImpl implements IGenreService {
    @Autowired
    private IGenreDao genreDao;

    @Override
    public List<Genre> findAll() {
        return (List<Genre>) this.genreDao.findAll();
    }

    @Override
    public Genre findById(Long id) {
        return this.genreDao.findById(id).orElse(null);
    }

    @Override
    public Genre save(Genre genre) {
        return this.genreDao.save(genre);
    }

    @Override
    public void delete(Long id) {
        this.genreDao.deleteById(id);
    }
}
