package com.ProyectoGPS.Backend.service.Impl;

import com.ProyectoGPS.Backend.dto.ParametroDto;
import com.ProyectoGPS.Backend.model.Parametro;
import com.ProyectoGPS.Backend.repository.ParametroRepository;
import com.ProyectoGPS.Backend.service.ParametroService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParametroServiceImpl implements ParametroService {
    private final ParametroRepository parametroRepo;

    @Override
    public ParametroDto crear(ParametroDto dto) {
        if (parametroRepo.existsById(dto.getClave())) {
            throw new EntityExistsException("Parámetro ya existe: " + dto.getClave());
        }
        Parametro p = new Parametro();
        p.setClave(dto.getClave());
        p.setValor(dto.getValor());
        p.setDescripcion(dto.getDescripcion());
        return mapToDto(parametroRepo.save(p));
    }

    @Override
    public List<ParametroDto> listarTodos() {
        return parametroRepo.findAll().stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    @Override
    public ParametroDto obtenerPorClave(String clave) {
        Parametro p = parametroRepo.findById(clave)
            .orElseThrow(() -> new EntityNotFoundException("Parámetro no encontrado: " + clave));
        return mapToDto(p);
    }

    @Override
    public ParametroDto actualizar(String clave, ParametroDto dto) {
        Parametro p = parametroRepo.findById(clave)
            .orElseThrow(() -> new EntityNotFoundException("Parámetro no encontrado: " + clave));
        p.setValor(dto.getValor());
        p.setDescripcion(dto.getDescripcion());
        return mapToDto(parametroRepo.save(p));
    }

    @Override
    public void eliminar(String clave) {
        if (!parametroRepo.existsById(clave)) {
            throw new EntityNotFoundException("Parámetro no encontrado: " + clave);
        }
        parametroRepo.deleteById(clave);
    }

    private ParametroDto mapToDto(Parametro p) {
        ParametroDto dto = new ParametroDto();
        dto.setClave(p.getClave());
        dto.setValor(p.getValor());
        dto.setDescripcion(p.getDescripcion());
        return dto;
    }
}