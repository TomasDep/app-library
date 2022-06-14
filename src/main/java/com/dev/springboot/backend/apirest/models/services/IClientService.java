package com.dev.springboot.backend.apirest.models.services;

import com.dev.springboot.backend.apirest.models.entities.Client;

import java.util.List;

public interface IClientService {
    public List<Client> findAll();
    public Client findById(Long id);
    public Client save(Client client);
    public void delete(Long id);
}
