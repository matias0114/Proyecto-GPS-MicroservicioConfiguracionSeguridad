package com.ProyectoGPS.Backend.repository;

import com.ProyectoGPS.Backend.model.Parametro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParametroRepository extends JpaRepository<Parametro, String> {
}