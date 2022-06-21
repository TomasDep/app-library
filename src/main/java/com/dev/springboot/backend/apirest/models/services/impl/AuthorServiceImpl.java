package com.dev.springboot.backend.apirest.models.services.impl;

import com.dev.springboot.backend.apirest.models.dao.IAuthorDao;
import com.dev.springboot.backend.apirest.models.entities.Author;
import com.dev.springboot.backend.apirest.models.services.IAuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements IAuthorService {
    @Autowired
    private IAuthorDao authorDao;

    @Override
    public List<Author> findAll() {
        return (List<Author>) this.authorDao.findAll();
    }

    @Override
    public Author findById(Long id) {
        return this.authorDao.findById(id).orElse(null);
    }

    @Override
    public Author save(Author author) {
        return this.authorDao.save(author);
    }

    @Override
    public void delete(Long id) {
        this.authorDao.deleteById(id);
    }
}
