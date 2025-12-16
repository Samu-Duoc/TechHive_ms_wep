package com.ms_auth_usuarios.dto;

import lombok.Data;

@Data
public class UpdateProfileDTO {

    private String nombre;

    private String apellido;

    private String rut;

    private String telefono;

    private String direccion;
}
