package com.example.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Column(name ="last_name",nullable = false)
    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @Column(nullable = false)
    @NotBlank(message = "Login is mandatory")
    private String login;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    @NotNull(message = "Role is mandatory")
    private Role role;

    public enum Role {
        Cliente, Administrador, Repartidor
    }


    @Column(nullable = false)
    @NotBlank(message = "Password is mandatory")
    private String password;
}