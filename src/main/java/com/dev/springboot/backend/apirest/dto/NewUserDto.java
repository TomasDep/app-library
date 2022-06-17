package com.dev.springboot.backend.apirest.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class NewUserDto {
    @NotBlank
    private String name;

    @NotBlank
    private String lastname;

    @NotBlank
    private String username;

    @Email
    private String email;

    @NotBlank
    private String password;

    private Set<String> roles = new HashSet<>();
}
