package com.dev.springboot.backend.apirest.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "authors")
@Getter
@Setter
@NoArgsConstructor
public class Author implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(nullable = false)
    private String name;

    @NotEmpty
    @Column(nullable = false)
    private String lastname;

    private String country;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = true)
    @JsonIgnore
    private List<Book> book;

    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;

    @Serial
    private static final long serialVersionUID = 1L;

    public Author(Long id, String name, String lastname, String country, Date createAt) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.country = country;
        this.createAt = createAt;
    }

    public Author(String name, String lastname, String country) {
        this.name = name;
        this.lastname = lastname;
        this.country = country;
    }

    @PrePersist
    public void PrePersist() {
        this.createAt = new Date();
    }
}
