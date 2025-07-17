package com.ProyectoGPS.Backend.service.Impl;

import com.ProyectoGPS.Backend.dto.RolDto;
import com.ProyectoGPS.Backend.model.Permiso;
import com.ProyectoGPS.Backend.model.Rol;
import com.ProyectoGPS.Backend.repository.PermisoRepository;
import com.ProyectoGPS.Backend.repository.RolRepository;
import com.ProyectoGPS.Backend.service.RolService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {
    private final RolRepository rolRepo;
    private final PermisoRepository permisoRepo;

    @Override
    public RolDto crear(RolDto dto) {
        Rol r = new Rol();
        r.setNombre(dto.getNombre());
        r.setPermisos(dto.getPermisos().stream()
            .map(nombre -> permisoRepo.findByNombre(nombre)
                .orElseThrow(() -> new EntityNotFoundException("Permiso no existe: " + nombre)))
            .collect(Collectors.toSet()));
        return mapToDto(rolRepo.save(r));
    }

    @Override
    public List<RolDto> listarTodos() {
        return rolRepo.findAll().stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    @Override
    public RolDto obtenerPorId(Long id) {
        Rol r = rolRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado: " + id));
        return mapToDto(r);
    }

    @Override
    public RolDto actualizar(Long id, RolDto dto) {
        Rol r = rolRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado: " + id));
        r.setNombre(dto.getNombre());
        r.setPermisos(dto.getPermisos().stream()
            .map(nombre -> permisoRepo.findByNombre(nombre)
                .orElseThrow(() -> new EntityNotFoundException("Permiso no existe: " + nombre)))
            .collect(Collectors.toSet()));
        return mapToDto(rolRepo.save(r));
    }

    @Override
    public void eliminar(Long id) {
        if (!rolRepo.existsById(id)) {
            throw new EntityNotFoundException("Rol no encontrado: " + id);
        }
        rolRepo.deleteById(id);
    }

    private RolDto mapToDto(Rol r) {
        RolDto dto = new RolDto();
        dto.setId(r.getId());
        dto.setNombre(r.getNombre());
        dto.setPermisos(r.getPermisos().stream()
            .map(Permiso::getNombre)
            .collect(Collectors.toSet()));
        return dto;
    }
}