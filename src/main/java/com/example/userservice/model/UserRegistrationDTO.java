package com.example.userservice.model;


import lombok.Data;

@Data
public class UserRegistrationDTO {


    private String name;


    private String email;


    private String password;

    private User.Role role;
}