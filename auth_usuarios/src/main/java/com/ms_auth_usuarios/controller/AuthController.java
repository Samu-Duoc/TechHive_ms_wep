package com.ms_auth_usuarios.controller;

import com.ms_auth_usuarios.dto.AuthResponseDTO;
import com.ms_auth_usuarios.dto.LoginRequestDTO;
import com.ms_auth_usuarios.dto.RecuperarClaveSeguraDTO;
import com.ms_auth_usuarios.dto.RegistroUsuarioDTO;
import com.ms_auth_usuarios.dto.SecurityQuestionDTO;
import com.ms_auth_usuarios.dto.UsuarioDTO;
import com.ms_auth_usuarios.service.UsuarioService;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor

public class AuthController {

    private final UsuarioService usuarioService;

    //Registro de Usuario
    @PostMapping("/registro")
    public ResponseEntity<UsuarioDTO> resgistrar (@Valid @RequestBody RegistroUsuarioDTO dto) {
        UsuarioDTO creado = usuarioService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    //Inicio de Sesión
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(usuarioService.loginConToken(dto));
    }


    //Recuperar Clave
    @PostMapping("/recuperar-clave-segura")
    public ResponseEntity<String> recuperarClaveSegura(@Valid @RequestBody RecuperarClaveSeguraDTO dto) {
        usuarioService.actualizarPasswordPorEmailConRespuesta(dto);
        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }

        @GetMapping("/pregunta-seguridad")
    public ResponseEntity<SecurityQuestionDTO> pregunta(@RequestParam String email) {
        String pregunta = usuarioService.obtenerPreguntaSeguridad(email);
        return ResponseEntity.ok(new SecurityQuestionDTO(pregunta));
    }


}
