package com.dev.springboot.backend.apirest.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserDto {
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
}
