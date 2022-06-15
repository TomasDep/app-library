package com.dev.springboot.backend.apirest.models.dao;

import com.dev.springboot.backend.apirest.models.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface IUserDao extends CrudRepository<User, Long> {
}
