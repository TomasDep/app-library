package com.dev.springboot.backend.apirest.models.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "clients")
@Getter
@Setter
public class Client implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String lastname;
    private String email;
    private String address;
    private String phone;

    @Column(name = "create_at")
    @Temporal(TemporalType.DATE)
    private Date createAt;

    @Serial
    private static final long serialVersionUID = 1L;

    @PrePersist
    public void PrePresist() {
        this.createAt = new Date();
    }
}