package com.ProyectoGPS.Backend;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.ProyectoGPS.Backend.dto.PermisoDto;
import com.ProyectoGPS.Backend.model.Permiso;
import com.ProyectoGPS.Backend.repository.PermisoRepository;
import com.ProyectoGPS.Backend.service.Impl.PermisoServiceImpl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

class PermisoServiceTest {

    @Mock
    private PermisoRepository permisoRepo;

    @InjectMocks
    private PermisoServiceImpl permisoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private PermisoDto getDto() {
        PermisoDto dto = new PermisoDto();
        dto.setId(1L);
        dto.setNombre("PERMISO_TEST");
        return dto;
    }

    private Permiso getEntity() {
        Permiso p = new Permiso();
        p.setId(1L);
        p.setNombre("PERMISO_TEST");
        return p;
    }

    @Test
    void testCrear_ok() {
        PermisoDto dto = getDto();

        when(permisoRepo.findByNombre("PERMISO_TEST")).thenReturn(Optional.empty());
        when(permisoRepo.save(any(Permiso.class))).thenReturn(getEntity());

        PermisoDto creado = permisoService.crear(dto);

        assertEquals("PERMISO_TEST", creado.getNombre());
        assertEquals(1L, creado.getId());
        verify(permisoRepo, times(1)).save(any(Permiso.class));
    }

    @Test
    void testCrear_permisoYaExiste() {
        PermisoDto dto = getDto();
        when(permisoRepo.findByNombre("PERMISO_TEST")).thenReturn(Optional.of(getEntity()));

        assertThrows(EntityExistsException.class, () -> permisoService.crear(dto));
        verify(permisoRepo, never()).save(any());
    }

    @Test
    void testListarTodos() {
        Permiso p1 = new Permiso();
        p1.setId(1L); p1.setNombre("PERMISO_1");
        Permiso p2 = new Permiso();
        p2.setId(2L); p2.setNombre("PERMISO_2");

        when(permisoRepo.findAll()).thenReturn(List.of(p1, p2));

        List<PermisoDto> lista = permisoService.listarTodos();

        assertEquals(2, lista.size());
        assertTrue(lista.stream().anyMatch(dto -> dto.getNombre().equals("PERMISO_1")));
        assertTrue(lista.stream().anyMatch(dto -> dto.getNombre().equals("PERMISO_2")));
    }

    @Test
    void testObtenerPorId_ok() {
        Permiso p = getEntity();
        when(permisoRepo.findById(1L)).thenReturn(Optional.of(p));

        PermisoDto dto = permisoService.obtenerPorId(1L);

        assertEquals("PERMISO_TEST", dto.getNombre());
        assertEquals(1L, dto.getId());
    }

    @Test
    void testObtenerPorId_noExiste() {
        when(permisoRepo.findById(9L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> permisoService.obtenerPorId(9L));
    }

    @Test
    void testActualizar_ok() {
        Permiso existente = getEntity();
        PermisoDto update = new PermisoDto();
        update.setNombre("PERMISO_EDITADO");
        update.setId(1L);

        // El repo devuelve el permiso existente
        when(permisoRepo.findById(1L)).thenReturn(Optional.of(existente));
        // Al guardar, devuelve el permiso actualizado
        Permiso actualizado = new Permiso();
        actualizado.setId(1L);
        actualizado.setNombre("PERMISO_EDITADO");
        when(permisoRepo.save(any(Permiso.class))).thenReturn(actualizado);

        PermisoDto result = permisoService.actualizar(1L, update);

        assertEquals("PERMISO_EDITADO", result.getNombre());
        assertEquals(1L, result.getId());
        verify(permisoRepo, times(1)).save(any(Permiso.class));
    }

    @Test
    void testActualizar_noExiste() {
        PermisoDto update = getDto();
        when(permisoRepo.findById(100L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> permisoService.actualizar(100L, update));
    }

    @Test
    void testEliminar_ok() {
        when(permisoRepo.existsById(1L)).thenReturn(true);
        permisoService.eliminar(1L);
        verify(permisoRepo, times(1)).deleteById(1L);
    }

    @Test
    void testEliminar_noExiste() {
        when(permisoRepo.existsById(999L)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> permisoService.eliminar(999L));
    }
}