package com.example.userservice.controller;

import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    // Inyección de dependencias (Spring lo hace automáticamente)
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Endpoint para obtener todos los usuarios
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Endpoint para crear un usuario
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }
}