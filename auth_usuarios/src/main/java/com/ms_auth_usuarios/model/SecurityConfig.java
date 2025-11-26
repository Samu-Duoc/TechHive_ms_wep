package com.ms_auth_usuarios.model;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

//ESTO PERMITE HASHEAR CONTRASEÑAS USANDO BCRYPT
//OSEA QUE SE VEA ASI ** EN LA BASE DE DATOS