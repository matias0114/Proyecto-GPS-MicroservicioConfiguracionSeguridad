package com.ProyectoGPS.Backend;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

import com.ProyectoGPS.Backend.dto.RolDto;
import com.ProyectoGPS.Backend.model.Permiso;
import com.ProyectoGPS.Backend.model.Rol;
import com.ProyectoGPS.Backend.repository.PermisoRepository;
import com.ProyectoGPS.Backend.repository.RolRepository;
import com.ProyectoGPS.Backend.service.Impl.RolServiceImpl;

import jakarta.persistence.EntityNotFoundException;

class RolServiceTest {

    @Mock
    private RolRepository rolRepo;

    @Mock
    private PermisoRepository permisoRepo;

    @InjectMocks
    private RolServiceImpl rolService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private RolDto getDto() {
        RolDto dto = new RolDto();
        dto.setId(1L);
        dto.setNombre("ROL_TEST");
        dto.setPermisos(Set.of("PERMISO_TEST"));
        return dto;
    }

    private Permiso getPermisoEntity() {
        Permiso p = new Permiso();
        p.setId(1L);
        p.setNombre("PERMISO_TEST");
        return p;
    }

    private Rol getRolEntity() {
        Rol r = new Rol();
        r.setId(1L);
        r.setNombre("ROL_TEST");
        r.setPermisos(Set.of(getPermisoEntity()));
        return r;
    }

    @Test
    void testCrear_ok() {
        RolDto dto = getDto();
        Permiso permiso = getPermisoEntity();

        when(permisoRepo.findByNombre("PERMISO_TEST")).thenReturn(Optional.of(permiso));
        when(rolRepo.save(any(Rol.class))).thenReturn(getRolEntity());

        RolDto creado = rolService.crear(dto);

        assertEquals("ROL_TEST", creado.getNombre());
        assertTrue(creado.getPermisos().contains("PERMISO_TEST"));
        verify(rolRepo, times(1)).save(any(Rol.class));
    }

    @Test
    void testCrear_permisoNoExiste() {
        RolDto dto = getDto();
        when(permisoRepo.findByNombre("PERMISO_TEST")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> rolService.crear(dto));
        verify(rolRepo, never()).save(any());
    }

    @Test
    void testListarTodos() {
        Rol r1 = new Rol();
        r1.setId(1L); r1.setNombre("ROL_1");
        Permiso p1 = new Permiso(); p1.setNombre("P1");
        r1.setPermisos(Set.of(p1));

        Rol r2 = new Rol();
        r2.setId(2L); r2.setNombre("ROL_2");
        Permiso p2 = new Permiso(); p2.setNombre("P2");
        r2.setPermisos(Set.of(p2));

        when(rolRepo.findAll()).thenReturn(List.of(r1, r2));

        List<RolDto> lista = rolService.listarTodos();

        assertEquals(2, lista.size());
        assertTrue(lista.stream().anyMatch(dto -> dto.getNombre().equals("ROL_1")));
        assertTrue(lista.stream().anyMatch(dto -> dto.getNombre().equals("ROL_2")));
    }

    @Test
    void testObtenerPorId_ok() {
        Rol r = getRolEntity();
        when(rolRepo.findById(1L)).thenReturn(Optional.of(r));

        RolDto dto = rolService.obtenerPorId(1L);

        assertEquals("ROL_TEST", dto.getNombre());
        assertTrue(dto.getPermisos().contains("PERMISO_TEST"));
    }

    @Test
    void testObtenerPorId_noExiste() {
        when(rolRepo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> rolService.obtenerPorId(99L));
    }

    @Test
    void testActualizar_ok() {
        Rol existente = getRolEntity();
        Permiso permisoNuevo = new Permiso();
        permisoNuevo.setId(2L);
        permisoNuevo.setNombre("PERMISO_NUEVO");

        RolDto update = new RolDto();
        update.setNombre("ROL_EDITADO");
        update.setPermisos(Set.of("PERMISO_NUEVO"));

        when(rolRepo.findById(1L)).thenReturn(Optional.of(existente));
        when(permisoRepo.findByNombre("PERMISO_NUEVO")).thenReturn(Optional.of(permisoNuevo));
        Rol actualizado = new Rol();
        actualizado.setId(1L);
        actualizado.setNombre("ROL_EDITADO");
        actualizado.setPermisos(Set.of(permisoNuevo));
        when(rolRepo.save(any(Rol.class))).thenReturn(actualizado);

        RolDto result = rolService.actualizar(1L, update);

        assertEquals("ROL_EDITADO", result.getNombre());
        assertTrue(result.getPermisos().contains("PERMISO_NUEVO"));
        verify(rolRepo, times(1)).save(any(Rol.class));
    }

    @Test
    void testActualizar_rolNoExiste() {
        RolDto update = getDto();
        when(rolRepo.findById(88L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> rolService.actualizar(88L, update));
    }

    @Test
    void testActualizar_permisoNoExiste() {
        Rol existente = getRolEntity();
        RolDto update = new RolDto();
        update.setNombre("ROL_X");
        update.setPermisos(Set.of("NO_PERMISO"));

        when(rolRepo.findById(1L)).thenReturn(Optional.of(existente));
        when(permisoRepo.findByNombre("NO_PERMISO")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> rolService.actualizar(1L, update));
    }

    @Test
    void testEliminar_ok() {
        when(rolRepo.existsById(1L)).thenReturn(true);
        rolService.eliminar(1L);
        verify(rolRepo, times(1)).deleteById(1L);
    }

    @Test
    void testEliminar_noExiste() {
        when(rolRepo.existsById(404L)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> rolService.eliminar(404L));
    }
}