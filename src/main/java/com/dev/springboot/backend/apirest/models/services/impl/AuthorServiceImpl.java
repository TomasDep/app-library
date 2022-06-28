package com.dev.springboot.backend.apirest.models.services.impl;

import com.dev.springboot.backend.apirest.models.dao.IAuthorDao;
import com.dev.springboot.backend.apirest.models.entities.Author;
import com.dev.springboot.backend.apirest.models.services.IAuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorServiceImpl implements IAuthorService {
    @Autowired
    private IAuthorDao authorDao;

    @Override
    @Transactional(readOnly = true)
    public Page<Author> findAll(int pageNumber, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNumber, pageSize);
        return this.authorDao.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Author findById(Long id) {
        return this.authorDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Author save(Author author) {
        return this.authorDao.save(author);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        this.authorDao.deleteById(id);
    }
}
