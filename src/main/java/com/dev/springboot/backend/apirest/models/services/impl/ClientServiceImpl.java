package com.dev.springboot.backend.apirest.models.services.impl;

import com.dev.springboot.backend.apirest.models.dao.IClientDao;
import com.dev.springboot.backend.apirest.models.entities.Client;
import com.dev.springboot.backend.apirest.models.services.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientServiceImpl implements IClientService {
    @Autowired
    private IClientDao clientDao;

    @Override
    @Transactional(readOnly = true)

    public Page<Client> findAll(int pageNumber, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNumber, pageSize);
        return this.clientDao.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Client findById(Long id) {
        return this.clientDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Client save(Client client) {
        return this.clientDao.save(client);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        this.clientDao.deleteById(id);
    }
}
