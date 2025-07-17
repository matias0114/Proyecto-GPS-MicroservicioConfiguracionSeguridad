package com.ProyectoGPS.Backend.service;

import com.ProyectoGPS.Backend.dto.ParametroDto;
import java.util.List;

public interface ParametroService {
    ParametroDto crear(ParametroDto dto);
    List<ParametroDto> listarTodos();
    ParametroDto obtenerPorClave(String clave);
    ParametroDto actualizar(String clave, ParametroDto dto);
    void eliminar(String clave);
}