package com.ProyectoGPS.Backend.service;

import com.ProyectoGPS.Backend.dto.RolDto;
import java.util.List;

public interface RolService {
    RolDto crear(RolDto dto);
    List<RolDto> listarTodos();
    RolDto obtenerPorId(Long id);
    RolDto actualizar(Long id, RolDto dto);
    void eliminar(Long id);
}