package com.dev.springboot.backend.apirest.models.services;

import com.dev.springboot.backend.apirest.enums.RoleEnum;
import com.dev.springboot.backend.apirest.models.entities.Role;

import java.util.Optional;

public interface IRoleService {
    public Optional<Role> getByRoleName(RoleEnum roleName);
}
