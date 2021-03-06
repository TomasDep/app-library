package com.dev.springboot.backend.apirest.models.entities;

import com.dev.springboot.backend.apirest.enums.BookStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "books")
@NoArgsConstructor
@Getter
@Setter
public class Book implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(nullable = false)
    private String name;

    @NotEmpty
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BookStatusEnum status;

    private String observation;
    private Integer stock;

    @NotNull
    private Double price;

    @NotNull
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "book_genre",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genre;

    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;

    @Serial
    private static final long serialVersionUID = 1L;

    public Book(
            String name,
            String description,
            BookStatusEnum status,
            String observation,
            Integer stock,
            Double price,
            Set<Genre> genre
    ) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.observation = observation;
        this.stock = stock;
        this.price = price;
        this.genre = genre;
    }

    @PrePersist
    public void PrePersist() {
        this.createAt = new Date();
    }
}
