package com.example.userservice.service;

import com.example.userservice.entity.UserInfo;
import com.example.userservice.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.userservice.repository.UserInfoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserInfoRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    public String addUser(UserInfo userInfo){
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return "User Added !!";
    }

    public void validateToken(String token){
        jwtService.validateToken(token);

    }

    public String generateToken(String userName){
        return jwtService.generateToken(userName);
    }


    // Admin methods
    public List<UserInfo> getAllUsers() {
        return repository.findAll();
    }

    public UserInfo getUserById(Integer id) {
        Optional<UserInfo> user = repository.findById(id);
        return user.orElse(null);
    }

    public String updateUser(Integer id, UserInfo userInfo) {
        Optional<UserInfo> existingUser = repository.findById(id);
        if (existingUser.isPresent()) {
            UserInfo user = existingUser.get();
            user.setName(userInfo.getName());
            user.setLastName(userInfo.getLastName());
            user.setLogin(userInfo.getLogin());
            user.setRole(userInfo.getRole());
            if (userInfo.getPassword() != null && !userInfo.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userInfo.getPassword()));
            }
            repository.save(user);
            return "User updated successfully";
        }
        return "User not found";
    }

    public String deleteUser(Integer id) {
        Optional<UserInfo> user = repository.findById(id);
        if (user.isPresent()) {
            repository.deleteById(id);
            return "User deleted successfully";
        }
        return "User not found";
    }
}
