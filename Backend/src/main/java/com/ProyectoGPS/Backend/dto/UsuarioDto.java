package com.ProyectoGPS.Backend.dto;

import lombok.Data;
import java.util.Set;

@Data
public class UsuarioDto {
    private Long id;

    
    private String username;

    
    private String password;

    
    private String fullName;

    
    private String email;

    
    private Set<String> roles;
}