package com.ProyectoGPS.Backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "system_parameters")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Parametro {
    @Id
    @Column(name = "param_key", nullable = false, unique = true)
    private String clave;

    @Column(name = "param_value", nullable = false)
    private String valor;

    private String descripcion;
}