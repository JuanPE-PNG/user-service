package com.example.userservice.conftoller;

import com.example.userservice.entity.UserInfo;
import com.example.userservice.service.AuthService;
import com.example.userservice.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/validateAdmin")
    public ResponseEntity<String> validateAdmin(@RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        if (jwtService.isAdminUser(jwtToken)) {
            return ResponseEntity.ok("Admin user validated");
        } else {
            return ResponseEntity.status(403).body("User is not an admin");
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserInfo>> getAllUsers(@RequestHeader("Authorization") String token) {

        String jwtToken = token.replace("Bearer ", "");

        if (!jwtService.isAdminUser(jwtToken)) {
            return ResponseEntity.status(403).build();
        }

        List<UserInfo> users = authService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserInfo> getUserById(@PathVariable Integer id,
                                                @RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");

        if (!jwtService.isAdminUser(jwtToken)) {
            return ResponseEntity.status(403).build();
        }

        UserInfo user = authService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Integer id,
                                             @RequestBody UserInfo userInfo,
                                             @RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");

        if (!jwtService.isAdminUser(jwtToken)) {
            return ResponseEntity.status(403).build();
        }

        String result = authService.updateUser(id, userInfo);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id,
                                             @RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");

        if (!jwtService.isAdminUser(jwtToken)) {
            return ResponseEntity.status(403).build();
        }

        String result = authService.deleteUser(id);
        return ResponseEntity.ok(result);
    }
}