package com.dev.springboot.backend.apirest.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(min = 2, max = 20)
    @Column(nullable = false)
    private String name;

    @NotEmpty
    @Size(min = 2, max = 20)
    @Column(nullable = false)
    private String lastname;

    @NotEmpty
    @Size(min = 2, max = 20)
    @Column(nullable = false, unique = true)
    private String username;

    @NotEmpty
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotEmpty
    @Size(min = 6, max = 16)
    @Column(nullable = false)
    private String password;

    private Boolean enabled;

    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;

    private static final long serialVersionUID = 1L;

    @PrePersist
    public void PrePersist() {
        this.createAt = new Date();
    }
}