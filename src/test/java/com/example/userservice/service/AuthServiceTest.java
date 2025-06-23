package com.example.userservice.service;

import com.example.userservice.entity.UserInfo;
import com.example.userservice.entity.UserInfo.Role;
import com.example.userservice.repository.UserInfoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.Collections;

class AuthServiceTest {
    @Mock
    private UserInfoRepository repository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private AuthService authService;

    public AuthServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddUser() {
        UserInfo user = new UserInfo(null, "Ana", "Gomez", "anagomez", Role.Cliente, "password123");
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(repository.save(any(UserInfo.class))).thenReturn(user);

        String result = authService.addUser(user);
        assertEquals("User Added !!", result);
        verify(repository, times(1)).save(any(UserInfo.class));
    }

    @Test
    void testGetAllUsers() {
        UserInfo user1 = new UserInfo(1, "Ana", "Gomez", "anagomez", Role.Cliente, "password123");
        UserInfo user2 = new UserInfo(2, "Luis", "Martinez", "luism", Role.Administrador, "pass456");
        when(repository.findAll()).thenReturn(Arrays.asList(user1, user2));
        assertEquals(2, authService.getAllUsers().size());
    }

    @Test
    void testGetUserByIdFound() {
        UserInfo user = new UserInfo(1, "Ana", "Gomez", "anagomez", Role.Cliente, "password123");
        when(repository.findById(1)).thenReturn(Optional.of(user));
        assertEquals(user, authService.getUserById(1));
    }

    @Test
    void testGetUserByIdNotFound() {
        when(repository.findById(99)).thenReturn(Optional.empty());
        assertNull(authService.getUserById(99));
    }

    @Test
    void testUpdateUserFound() {
        UserInfo existing = new UserInfo(1, "Ana", "Gomez", "anagomez", Role.Cliente, "password123");
        UserInfo update = new UserInfo(null, "AnaMaria", "Gomez", "anagomez", Role.Cliente, "newpass");
        when(repository.findById(1)).thenReturn(Optional.of(existing));
        when(passwordEncoder.encode("newpass")).thenReturn("encodedNewPass");
        when(repository.save(any(UserInfo.class))).thenReturn(existing);
        String result = authService.updateUser(1, update);
        assertEquals("User updated successfully", result);
    }

    @Test
    void testUpdateUserNotFound() {
        UserInfo update = new UserInfo(null, "AnaMaria", "Gomez", "anagomez", Role.Cliente, "newpass");
        when(repository.findById(99)).thenReturn(Optional.empty());
        String result = authService.updateUser(99, update);
        assertEquals("User not found", result);
    }

    @Test
    void testDeleteUserFound() {
        UserInfo user = new UserInfo(1, "Ana", "Gomez", "anagomez", Role.Cliente, "password123");
        when(repository.findById(1)).thenReturn(Optional.of(user));
        String result = authService.deleteUser(1);
        assertEquals("User deleted successfully", result);
        verify(repository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteUserNotFound() {
        when(repository.findById(99)).thenReturn(Optional.empty());
        String result = authService.deleteUser(99);
        assertEquals("User not found", result);
    }
} 