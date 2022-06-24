package com.dev.springboot.backend.apirest.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
public class Client implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(min = 2, max = 20)
    @Column(nullable = false)
    private String name;

    @NotEmpty
    @Column(nullable = false)
    private String lastname;

    @NotEmpty
    @Email
    @Column(nullable = false, unique = true)
    private String email;
    private String address;
    private String phone;

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