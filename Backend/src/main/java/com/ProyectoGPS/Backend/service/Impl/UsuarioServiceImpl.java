package com.ProyectoGPS.Backend.service.Impl;

import com.ProyectoGPS.Backend.dto.UsuarioDto;
import com.ProyectoGPS.Backend.model.Usuario;
import com.ProyectoGPS.Backend.model.Rol;
import com.ProyectoGPS.Backend.repository.UsuarioRepository;
import com.ProyectoGPS.Backend.repository.RolRepository;
import com.ProyectoGPS.Backend.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepo;
    private final RolRepository rolRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UsuarioDto crear(UsuarioDto dto) {
        Usuario u = new Usuario();
        u.setUsername(dto.getUsername());
        u.setPassword(passwordEncoder.encode(dto.getPassword()));
        u.setFullName(dto.getFullName());
        u.setEmail(dto.getEmail());
        u.setRoles(dto.getRoles().stream()
            .map(nombre -> rolRepo.findByNombre(nombre)
                .orElseThrow(() -> new EntityNotFoundException("Rol no existe: " + nombre)))
            .collect(Collectors.toSet()));
        Usuario saved = usuarioRepo.save(u);
        return mapToDto(saved);
    }

    @Override
    public List<UsuarioDto> listarTodos() {
        return usuarioRepo.findAll().stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    @Override
    public UsuarioDto obtenerPorId(Long id) {
        Usuario u = usuarioRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + id));
        return mapToDto(u);
    }

    @Override
    public UsuarioDto actualizar(Long id, UsuarioDto dto) {
        Usuario u = usuarioRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + id));
        u.setFullName(dto.getFullName());
        u.setEmail(dto.getEmail());
        u.setRoles(dto.getRoles().stream()
            .map(nombre -> rolRepo.findByNombre(nombre)
                .orElseThrow(() -> new EntityNotFoundException("Rol no existe: " + nombre)))
            .collect(Collectors.toSet()));
        return mapToDto(usuarioRepo.save(u));
    }

    @Override
    public void eliminar(Long id) {
        if (!usuarioRepo.existsById(id)) {
            throw new EntityNotFoundException("Usuario no encontrado: " + id);
        }
        usuarioRepo.deleteById(id);
    }

    private UsuarioDto mapToDto(Usuario u) {
        UsuarioDto dto = new UsuarioDto();
        dto.setId(u.getId());
        dto.setUsername(u.getUsername());
        dto.setFullName(u.getFullName());
        dto.setEmail(u.getEmail());
        dto.setRoles(u.getRoles().stream()
            .map(Rol::getNombre)
            .collect(Collectors.toSet()));
        return dto;
    }
}