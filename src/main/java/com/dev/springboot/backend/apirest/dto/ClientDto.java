package com.dev.springboot.backend.apirest.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ClientDto {
    @NotBlank
    private String name;

    @NotBlank
    private String lastname;

    @Email
    private String email;

    private String address;
    private String phone;
}
