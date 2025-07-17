package com.ProyectoGPS.Backend.service;

import com.ProyectoGPS.Backend.dto.UsuarioDto;
import java.util.List;

public interface UsuarioService {
    UsuarioDto crear(UsuarioDto dto);
    List<UsuarioDto> listarTodos();
    UsuarioDto obtenerPorId(Long id);
    UsuarioDto actualizar(Long id, UsuarioDto dto);
    void eliminar(Long id);
    UsuarioDto signup(UsuarioDto dto); // registro de usuarios
}