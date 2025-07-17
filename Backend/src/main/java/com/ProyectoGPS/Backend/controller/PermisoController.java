// src/main/java/com/ProyectoGPS/Backend/controller/PermisoController.java
package com.ProyectoGPS.Backend.controller;

import com.ProyectoGPS.Backend.dto.PermisoDto;
import com.ProyectoGPS.Backend.service.PermisoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/config/permisos")
@RequiredArgsConstructor
public class PermisoController {

    private final PermisoService permisoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PermisoDto crear(@RequestBody  PermisoDto dto) {
        return permisoService.crear(dto);
    }

    @GetMapping
    public List<PermisoDto> listarTodos() {
        return permisoService.listarTodos();
    }

    @GetMapping("/{id}")
    public PermisoDto obtenerPorId(@PathVariable Long id) {
        return permisoService.obtenerPorId(id);
    }

    @PutMapping("/{id}")
    public PermisoDto actualizar(
            @PathVariable Long id,
            @RequestBody  PermisoDto dto) {
        return permisoService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        permisoService.eliminar(id);
    }
}
