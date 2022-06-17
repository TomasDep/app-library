package com.dev.springboot.backend.apirest.models.entities;

import com.dev.springboot.backend.apirest.enums.RoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RoleEnum roleName;

    public Role (RoleEnum roleName) {
        this.roleName = roleName;
    }
}
