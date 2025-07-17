package com.ProyectoGPS.Backend.service;

import com.ProyectoGPS.Backend.dto.PermisoDto;
import java.util.List;

public interface PermisoService {
    PermisoDto crear(PermisoDto dto);
    List<PermisoDto> listarTodos();
    PermisoDto obtenerPorId(Long id);
    PermisoDto actualizar(Long id, PermisoDto dto);
    void eliminar(Long id);
}