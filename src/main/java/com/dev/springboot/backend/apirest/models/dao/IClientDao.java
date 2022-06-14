package com.dev.springboot.backend.apirest.models.dao;

import com.dev.springboot.backend.apirest.models.entities.Client;
import org.springframework.data.repository.CrudRepository;

public interface IClientDao extends CrudRepository<Client, Long> {
}
