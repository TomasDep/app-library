package com.dev.springboot.backend.apirest.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private List<Book> book;

    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;

    @Serial
    private static final long serialVersionUID = 1L;

    @PrePersist
    public void PrePersist() {
        this.createAt = new Date();
    }
}
