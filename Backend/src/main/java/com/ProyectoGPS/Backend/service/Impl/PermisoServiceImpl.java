package com.ProyectoGPS.Backend.service.Impl;

import com.ProyectoGPS.Backend.dto.PermisoDto;
import com.ProyectoGPS.Backend.model.Permiso;
import com.ProyectoGPS.Backend.repository.PermisoRepository;
import com.ProyectoGPS.Backend.service.PermisoService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermisoServiceImpl implements PermisoService {
    private final PermisoRepository permisoRepo;

    @Override
    public PermisoDto crear(PermisoDto dto) {
        if (permisoRepo.findByNombre(dto.getNombre()).isPresent()) {
            throw new EntityExistsException("Permiso ya existe: " + dto.getNombre());
        }
        Permiso p = new Permiso();
        p.setNombre(dto.getNombre());
        return mapToDto(permisoRepo.save(p));
    }

    @Override
    public List<PermisoDto> listarTodos() {
        return permisoRepo.findAll().stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    @Override
    public PermisoDto obtenerPorId(Long id) {
        Permiso p = permisoRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Permiso no encontrado: " + id));
        return mapToDto(p);
    }

    @Override
    public PermisoDto actualizar(Long id, PermisoDto dto) {
        Permiso p = permisoRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Permiso no encontrado: " + id));
        p.setNombre(dto.getNombre());
        return mapToDto(permisoRepo.save(p));
    }

    @Override
    public void eliminar(Long id) {
        if (!permisoRepo.existsById(id)) {
            throw new EntityNotFoundException("Permiso no encontrado: " + id);
        }
        permisoRepo.deleteById(id);
    }

    private PermisoDto mapToDto(Permiso p) {
        PermisoDto dto = new PermisoDto();
        dto.setId(p.getId());
        dto.setNombre(p.getNombre());
        return dto;
    }
}