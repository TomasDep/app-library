package com.dev.springboot.backend.apirest.models.repository;

import com.dev.springboot.backend.apirest.enums.RoleEnum;
import com.dev.springboot.backend.apirest.models.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(RoleEnum roleEnum);
}
