package com.example.userservice.conftoller;

import com.example.userservice.entity.UserInfo;
import com.example.userservice.entity.UserInfo.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.example.userservice.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import com.example.userservice.dto.AuthRequest;
import org.springframework.security.core.Authentication;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.containsString;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    void testRegisterUser() throws Exception {
        UserInfo user = new UserInfo(null, "Juan", "Pérez", "juanperez", Role.Cliente, "password123");
        when(authService.addUser(user)).thenReturn("User Added !!");

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/auth/registerUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().string("User Added !!"));
    }

    @Test
    void testGenerateToken() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setName("juanperez");
        authRequest.setPassword("password123");
        String token = "fake-jwt-token";
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authService.generateToken("juanperez")).thenReturn(token);

        ObjectMapper objectMapper = new ObjectMapper();
        String authJson = objectMapper.writeValueAsString(authRequest);

        mockMvc.perform(post("/auth/generateToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(authJson))
                .andExpect(status().isOk())
                .andExpect(content().string(token));
    }

    @Test
    void testGenerateTokenInvalidCredentials() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setName("juanperez");
        authRequest.setPassword("wrongpassword");
        when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException("Invalid credential."));

        ObjectMapper objectMapper = new ObjectMapper();
        String authJson = objectMapper.writeValueAsString(authRequest);

        mockMvc.perform(post("/auth/generateToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(authJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testValidateToken() throws Exception {
        String token = "fake-jwt-token";
        // No exception thrown means valid
        mockMvc.perform(get("/auth/validateToken")
                .param("token", token))
                .andExpect(status().isOk())
                .andExpect(content().string("Token is valid."));
    }

    @Test
    void testRegisterUserWithDuplicateLogin() throws Exception {
        UserInfo user = new UserInfo(null, "Juan", "Pérez", "juanperez", Role.Cliente, "password123");
        when(authService.addUser(user)).thenThrow(new RuntimeException("Login already exists"));
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/auth/registerUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("Login already exists")));
    }

    @Test
    void testValidateTokenInvalid() throws Exception {
        String token = "invalid-token";
        doThrow(new RuntimeException("Invalid token")).when(authService).validateToken(token);
        mockMvc.perform(get("/auth/validateToken")
                .param("token", token))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("Invalid token")));
    }
} 