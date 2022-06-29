package com.dev.springboot.backend.apirest.models.services.impl;

import com.dev.springboot.backend.apirest.models.dao.IBookDao;
import com.dev.springboot.backend.apirest.models.entities.Book;
import com.dev.springboot.backend.apirest.models.services.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookServiceImpl implements IBookService {
    @Autowired
    private IBookDao bookDao;

    @Override
    @Transactional(readOnly = true)
    public Page<Book> findAll(int pageNumber, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNumber, pageSize);
        return this.bookDao.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Book findById(Long id) {
        return this.bookDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Book save(Book book) {
        return this.bookDao.save(book);
    }

    @Override
    public void delete(Long id) {
        this.bookDao.deleteById(id);
    }
}
