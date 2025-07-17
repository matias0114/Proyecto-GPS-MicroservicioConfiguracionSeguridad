// src/main/java/com/ProyectoGPS/Backend/controller/RolController.java
package com.ProyectoGPS.Backend.controller;

import com.ProyectoGPS.Backend.dto.RolDto;
import com.ProyectoGPS.Backend.service.RolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/config/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolService rolService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RolDto crear(@RequestBody  RolDto dto) {
        return rolService.crear(dto);
    }

    @GetMapping
    public List<RolDto> listarTodos() {
        return rolService.listarTodos();
    }

    @GetMapping("/{id}")
    public RolDto obtenerPorId(@PathVariable Long id) {
        return rolService.obtenerPorId(id);
    }

    @PutMapping("/{id}")
    public RolDto actualizar(
            @PathVariable Long id,
            @RequestBody RolDto dto) {
        return rolService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        rolService.eliminar(id);
    }
}
