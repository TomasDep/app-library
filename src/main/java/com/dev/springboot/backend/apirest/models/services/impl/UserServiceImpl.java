package com.dev.springboot.backend.apirest.models.services.impl;

import com.dev.springboot.backend.apirest.models.dao.IUserDao;
import com.dev.springboot.backend.apirest.models.entities.User;
import com.dev.springboot.backend.apirest.models.repository.IUserRepository;
import com.dev.springboot.backend.apirest.models.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private IUserDao userDao;

    @Autowired
    private IUserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAll(int pageNumber, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNumber, pageSize);
        return this.userDao.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return this.userDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public User save(User user) {
        return this.userDao.save(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        this.userDao.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<User> getByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public boolean existsByUsername(String username) {
        return this.userRepository.existsByUsername(username);
    }

    @Override
    @Transactional
    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }
}
