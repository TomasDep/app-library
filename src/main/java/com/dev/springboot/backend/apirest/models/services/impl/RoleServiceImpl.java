package com.dev.springboot.backend.apirest.models.services;

import com.dev.springboot.backend.apirest.enums.RoleEnum;
import com.dev.springboot.backend.apirest.models.entities.Role;
import com.dev.springboot.backend.apirest.models.repository.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    IRoleRepository roleRepository;

    @Override
    public Optional<Role> getByRoleName(RoleEnum roleEnum) {
        return this.roleRepository.findByRoleName(roleEnum);
    }
}
