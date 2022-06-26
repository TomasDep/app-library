package com.dev.springboot.backend.apirest.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AuthorDto {
    @NotBlank
    private String name;

    @NotBlank
    private String lastname;

    @NotBlank
    private String country;
}