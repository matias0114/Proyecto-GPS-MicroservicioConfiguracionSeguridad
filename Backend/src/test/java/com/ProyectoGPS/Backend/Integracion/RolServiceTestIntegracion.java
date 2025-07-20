package com.ProyectoGPS.Backend.Integracion;

import com.ProyectoGPS.Backend.dto.RolDto;
import com.ProyectoGPS.Backend.model.Permiso;
import com.ProyectoGPS.Backend.model.Rol;
import com.ProyectoGPS.Backend.repository.PermisoRepository;
import com.ProyectoGPS.Backend.repository.RolRepository;
import com.ProyectoGPS.Backend.service.RolService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class RolServiceTestIntegracion {

    @Autowired
    private RolService rolService;

    @Autowired
    private PermisoRepository permisoRepository;

    @Autowired
    private RolRepository rolRepository;

    @BeforeEach
    void setup() {
        // Deja la base limpia antes de cada test (opcional porque @Transactional limpia al terminar)
        rolRepository.deleteAll();
        permisoRepository.deleteAll();

        // Crea algunos permisos base
        Permiso p1 = new Permiso();
        p1.setNombre("PERMISO_1");
        permisoRepository.save(p1);

        Permiso p2 = new Permiso();
        p2.setNombre("PERMISO_2");
        permisoRepository.save(p2);
    }

    @Test
    void testCrearYObtenerRol() {
        RolDto nuevoRol = new RolDto();
        nuevoRol.setNombre("ROL_ADMIN");
        nuevoRol.setPermisos(Set.of("PERMISO_1"));

        RolDto creado = rolService.crear(nuevoRol);

        assertNotNull(creado.getId());
        assertEquals("ROL_ADMIN", creado.getNombre());
        assertTrue(creado.getPermisos().contains("PERMISO_1"));

        // Obtener por id
        RolDto buscado = rolService.obtenerPorId(creado.getId());
        assertEquals("ROL_ADMIN", buscado.getNombre());
        assertTrue(buscado.getPermisos().contains("PERMISO_1"));
    }

    @Test
    void testActualizarRol() {
        // Primero creamos el rol
        RolDto nuevoRol = new RolDto();
        nuevoRol.setNombre("ROL_USER");
        nuevoRol.setPermisos(Set.of("PERMISO_1"));
        RolDto creado = rolService.crear(nuevoRol);

        // Ahora actualizamos el nombre y los permisos
        RolDto update = new RolDto();
        update.setNombre("ROL_USER_EDITADO");
        update.setPermisos(Set.of("PERMISO_2"));

        RolDto actualizado = rolService.actualizar(creado.getId(), update);

        assertEquals("ROL_USER_EDITADO", actualizado.getNombre());
        assertTrue(actualizado.getPermisos().contains("PERMISO_2"));
        assertFalse(actualizado.getPermisos().contains("PERMISO_1"));
    }

    @Test
    void testEliminarRol() {
        RolDto nuevoRol = new RolDto();
        nuevoRol.setNombre("ROL_BORRAR");
        nuevoRol.setPermisos(Set.of("PERMISO_1"));
        RolDto creado = rolService.crear(nuevoRol);

        // Verifica que existe antes de eliminar
        assertNotNull(rolService.obtenerPorId(creado.getId()));

        rolService.eliminar(creado.getId());

        assertThrows(EntityNotFoundException.class, () -> rolService.obtenerPorId(creado.getId()));
    }

    @Test
    void testListarTodosRoles() {
        // Crea dos roles
        RolDto r1 = new RolDto();
        r1.setNombre("ROL_1");
        r1.setPermisos(Set.of("PERMISO_1"));
        rolService.crear(r1);

        RolDto r2 = new RolDto();
        r2.setNombre("ROL_2");
        r2.setPermisos(Set.of("PERMISO_2"));
        rolService.crear(r2);

        List<RolDto> todos = rolService.listarTodos();

        assertTrue(todos.size() >= 2);
        assertTrue(todos.stream().anyMatch(r -> r.getNombre().equals("ROL_1")));
        assertTrue(todos.stream().anyMatch(r -> r.getNombre().equals("ROL_2")));
    }

    @Test
    void testCrearRolConPermisoInexistente() {
        RolDto rol = new RolDto();
        rol.setNombre("ROL_X");
        rol.setPermisos(Set.of("NO_EXISTE"));

        assertThrows(EntityNotFoundException.class, () -> rolService.crear(rol));
    }
}