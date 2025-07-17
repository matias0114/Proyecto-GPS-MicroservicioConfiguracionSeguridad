package com.ProyectoGPS.Backend.dto;

import lombok.Data;
import java.util.Set;

@Data
public class RolDto {
    private Long id;

    
    private String nombre;

    
    private Set<String> permisos;
}