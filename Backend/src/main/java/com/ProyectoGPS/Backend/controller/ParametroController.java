// src/main/java/com/ProyectoGPS/Backend/controller/ParametroController.java
package com.ProyectoGPS.Backend.controller;

import com.ProyectoGPS.Backend.dto.ParametroDto;
import com.ProyectoGPS.Backend.service.ParametroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/config/parametros")
@RequiredArgsConstructor
public class ParametroController {

    private final ParametroService parametroService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParametroDto crear(@RequestBody  ParametroDto dto) {
        return parametroService.crear(dto);
    }

    @GetMapping
    public List<ParametroDto> listarTodos() {
        return parametroService.listarTodos();
    }

    @GetMapping("/{clave}")
    public ParametroDto obtenerPorClave(@PathVariable String clave) {
        return parametroService.obtenerPorClave(clave);
    }

    @PutMapping("/{clave}")
    public ParametroDto actualizar(
            @PathVariable String clave,
            @RequestBody ParametroDto dto) {
        return parametroService.actualizar(clave, dto);
    }

    @DeleteMapping("/{clave}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable String clave) {
        parametroService.eliminar(clave);
    }
}
