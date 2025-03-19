package com.example.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",                // Ruta raíz
                                "/api/users/**",   // Endpoints de usuarios
                                "/h2-console/**",  // Consola H2 (por si acaso)
                                "/static/**",      // Recursos estáticos (CSS, JS, imágenes)
                                "/error"           // Página de error
                        ).permitAll() // Todas estas rutas son públicas
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame.disable())
                )
                .formLogin(form -> form.disable());

        return http.build();
    }
}