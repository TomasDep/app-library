package com.dev.springboot.backend.apirest.models.services;

import com.dev.springboot.backend.apirest.models.dao.IUserDao;
import com.dev.springboot.backend.apirest.models.entities.User;
import com.dev.springboot.backend.apirest.models.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private IUserDao userDao;

    @Autowired
    private IUserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>) this.userDao.findAll();
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
