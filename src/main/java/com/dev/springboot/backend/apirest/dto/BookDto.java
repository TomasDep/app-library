package com.dev.springboot.backend.apirest.dto;

import com.dev.springboot.backend.apirest.enums.BookStatusEnum;
import com.dev.springboot.backend.apirest.models.entities.Genre;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class BookDto {
    private String name;
    private String description;
    private BookStatusEnum status;
    private String observation;
    private Integer stock;
    private Double price;
    private Set<Genre> genre;
}
