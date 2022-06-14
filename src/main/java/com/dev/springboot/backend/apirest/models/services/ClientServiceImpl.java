package com.dev.springboot.backend.apirest.models.services;

import com.dev.springboot.backend.apirest.models.dao.IClientDao;
import com.dev.springboot.backend.apirest.models.entities.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientServiceImpl implements IClientService{
    @Autowired
    private IClientDao clientDao;

    @Override
    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return (List<Client>) this.clientDao.findAll();
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
