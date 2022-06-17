package com.dev.springboot.backend.apirest.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginDto {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
