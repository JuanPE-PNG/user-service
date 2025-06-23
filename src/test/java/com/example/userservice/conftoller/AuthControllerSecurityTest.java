package com.example.userservice.conftoller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.example.userservice.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = true)
class AuthControllerSecurityTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    void testValidateTokenRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/auth/validateToken").param("token", "any-token"))
                .andExpect(status().isUnauthorized());
    }
} 