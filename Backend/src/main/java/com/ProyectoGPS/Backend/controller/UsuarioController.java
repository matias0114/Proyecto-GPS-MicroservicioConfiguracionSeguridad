// src/main/java/com/ProyectoGPS/Backend/controller/UsuarioController.java
package com.ProyectoGPS.Backend.controller;

import com.ProyectoGPS.Backend.dto.UsuarioDto;
import com.ProyectoGPS.Backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/config/usuarios")
@RequiredArgsConstructor
@Validated
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioDto crear(@RequestBody UsuarioDto dto) {
        return usuarioService.crear(dto);
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioDto signup(@RequestBody UsuarioDto dto) {
        return usuarioService.signup(dto);
    }

    @GetMapping
    public List<UsuarioDto> listarTodos() {
        return usuarioService.listarTodos();
    }

    @GetMapping("/{id}")
    public UsuarioDto obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id);
    }

    @PutMapping("/{id}")
    public UsuarioDto actualizar(
            @PathVariable Long id,
            @RequestBody UsuarioDto dto) {
        return usuarioService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
    }
}