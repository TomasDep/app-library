package com.dev.springboot.backend.apirest.models.dao;

import com.dev.springboot.backend.apirest.models.entities.Client;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IClientDao extends PagingAndSortingRepository<Client, Long> {
}
