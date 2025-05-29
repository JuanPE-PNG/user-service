package com.example.userservice.conftoller;

import com.example.userservice.dto.AuthRequest;
import com.example.userservice.entity.UserInfo;
import com.example.userservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/registerUser")
    public String addUser(@RequestBody UserInfo userInfo){
        return authService.addUser(userInfo);
    }
    @PostMapping("/generateToken")
    public String generateToken(@RequestBody AuthRequest authRequest){
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getName(), authRequest.getPassword()));
        if(authenticate.isAuthenticated()){
            return authService.generateToken(authRequest.getName());
        }else{
            throw  new RuntimeException("Invalid credential.");
        }

    }
    @GetMapping("/validateToken")
    public String validateToken(@RequestParam("token") String token){
        authService.validateToken(token);

        return "Token is valid.";
    }
}
