package com.ms_pedidos_pagos.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class UsuarioClient {

    private final WebClient webClient;

    public UsuarioClient(@Value("${usuarios-service.url}") String usuariosServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(usuariosServiceUrl)
                .build();
    }

    public Map<String, Object> getUsuarioById(Long id, String authHeader) {
        WebClient.RequestHeadersSpec<?> req = this.webClient.get()
                .uri("/{id}", id);
        if (authHeader != null && !authHeader.isBlank()) {
            req = req.header("Authorization", authHeader);
        }
        return req.retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    // Backward-compatible overload (no auth header)
    public Map<String, Object> getUsuarioById(Long id) {
        return getUsuarioById(id, null);
    }
}
