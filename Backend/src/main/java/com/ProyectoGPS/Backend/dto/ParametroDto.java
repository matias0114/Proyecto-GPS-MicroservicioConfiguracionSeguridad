package com.ProyectoGPS.Backend.dto;

import lombok.Data;

@Data
public class ParametroDto {
    
    private String clave;

    
    private String valor;

    private String descripcion;
}
//no funciona el tema de validacion de jakarta.validation, por lo que se ha comentado la anotacion @Valid en los controladores
//para que no de error al compilar. Se debe revisar la configuracion de dependencias
//y la version de spring boot para que funcione correctamente la validacion de los DTOs