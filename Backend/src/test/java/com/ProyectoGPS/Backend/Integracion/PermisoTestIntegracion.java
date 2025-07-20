package com.ProyectoGPS.Backend.Integracion;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.ProyectoGPS.Backend.dto.PermisoDto;
import com.ProyectoGPS.Backend.service.PermisoService;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PermisoTestIntegracion {

    @Autowired
    private PermisoService permisoService;

    @Test
    void testCrearYObtenerPermiso() {
        PermisoDto nuevo = new PermisoDto();
        nuevo.setNombre("PERMISO_CREATE");

        PermisoDto creado = permisoService.crear(nuevo);

        assertNotNull(creado.getId());
        assertEquals("PERMISO_CREATE", creado.getNombre());

        PermisoDto buscado = permisoService.obtenerPorId(creado.getId());
        assertEquals("PERMISO_CREATE", buscado.getNombre());
    }

    @Test
    void testActualizarPermiso() {
        PermisoDto nuevo = new PermisoDto();
        nuevo.setNombre("PERMISO_EDIT");
        PermisoDto creado = permisoService.crear(nuevo);

        PermisoDto update = new PermisoDto();
        update.setNombre("PERMISO_EDITADO");

        PermisoDto actualizado = permisoService.actualizar(creado.getId(), update);

        assertEquals("PERMISO_EDITADO", actualizado.getNombre());
    }

    @Test
    void testEliminarPermiso() {
        PermisoDto nuevo = new PermisoDto();
        nuevo.setNombre("PERMISO_BORRAR");
        PermisoDto creado = permisoService.crear(nuevo);

        permisoService.eliminar(creado.getId());
        assertThrows(EntityNotFoundException.class, () -> permisoService.obtenerPorId(creado.getId()));
    }

    @Test
    void testCrearPermisoDuplicadoFalla() {
        PermisoDto nuevo = new PermisoDto();
        nuevo.setNombre("PERMISO_DUPLICADO");
        permisoService.crear(nuevo);

        PermisoDto repetido = new PermisoDto();
        repetido.setNombre("PERMISO_DUPLICADO");

        assertThrows(EntityExistsException.class, () -> permisoService.crear(repetido));
    }

    @Test
    void testListarTodos() {
        PermisoDto p1 = new PermisoDto();
        p1.setNombre("P1");
        permisoService.crear(p1);

        PermisoDto p2 = new PermisoDto();
        p2.setNombre("P2");
        permisoService.crear(p2);

        List<PermisoDto> todos = permisoService.listarTodos();
        assertTrue(todos.size() >= 2);
        assertTrue(todos.stream().anyMatch(p -> p.getNombre().equals("P1")));
        assertTrue(todos.stream().anyMatch(p -> p.getNombre().equals("P2")));
    }

    @Test
    void testObtenerPermisoNoExiste() {
        assertThrows(EntityNotFoundException.class, () -> permisoService.obtenerPorId(999L));
    }
}