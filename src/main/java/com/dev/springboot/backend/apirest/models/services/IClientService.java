package com.dev.springboot.backend.apirest.models.services;

import com.dev.springboot.backend.apirest.models.entities.Client;
import org.springframework.data.domain.Page;

public interface IClientService {
    public Page<Client> findAll(int pageNumber, int pageSize);
    public Client findById(Long id);
    public Client save(Client client);
    public void delete(Long id);
}
