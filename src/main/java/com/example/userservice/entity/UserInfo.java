package com.example.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name ="app_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name",nullable = false)
    private String name;

    @Column(name ="last_name",nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String login;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private Role role;

    public enum Role {
        Cliente, Administrador, Repartidor
    }


    @Column(nullable = false)
    private String password;
}