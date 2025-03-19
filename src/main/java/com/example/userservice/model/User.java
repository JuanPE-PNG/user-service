package com.example.userservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users") // <-- ¡Aquí está el cambio!
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        CLIENTE, ADMINISTRADOR, REPARTIDOR
    }
}