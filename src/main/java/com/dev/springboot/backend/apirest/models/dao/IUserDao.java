package com.dev.springboot.backend.apirest.models.dao;

import com.dev.springboot.backend.apirest.models.entities.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IUserDao extends PagingAndSortingRepository<User, Long> {
}
