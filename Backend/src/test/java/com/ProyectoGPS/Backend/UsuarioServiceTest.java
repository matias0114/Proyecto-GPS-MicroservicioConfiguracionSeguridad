package com.ProyectoGPS.Backend;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.ProyectoGPS.Backend.dto.UsuarioDto;
import com.ProyectoGPS.Backend.model.Rol;
import com.ProyectoGPS.Backend.repository.RolRepository;
import com.ProyectoGPS.Backend.service.UsuarioService;

import jakarta.persistence.EntityNotFoundException;

@SpringBootTest
@ActiveProfiles("test") 
@Transactional
class UsuarioServiceImplIT {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        if (rolRepository.findByNombre("ROLE_USER").isEmpty()) {
            Rol rol = new Rol();
            rol.setNombre("ROLE_USER");
            rolRepository.save(rol);
        }
        if (rolRepository.findByNombre("ROLE_ADMIN").isEmpty()) {
            Rol rol = new Rol();
            rol.setNombre("ROLE_ADMIN");
            rolRepository.save(rol);
        }
    }

    @Test
    void crearYObtenerUsuarioTest() {
        UsuarioDto nuevo = new UsuarioDto();
        nuevo.setUsername("matias");
        nuevo.setPassword("1234");
        nuevo.setFullName("Matias Jara");
        nuevo.setEmail("matias@correo.com");
        nuevo.setRoles(Set.of("ROLE_USER"));

        UsuarioDto creado = usuarioService.crear(nuevo);

        assertNotNull(creado.getId());
        assertEquals("matias", creado.getUsername());
        assertTrue(creado.getRoles().contains("ROLE_USER"));

        UsuarioDto obtenido = usuarioService.obtenerPorId(creado.getId());
        assertEquals("matias", obtenido.getUsername());
    }

    @Test
    void eliminarUsuarioTest() {
        UsuarioDto nuevo = new UsuarioDto();
        nuevo.setUsername("borrar");
        nuevo.setPassword("pw");
        nuevo.setFullName("Borrar Usuario");
        nuevo.setEmail("borrar@correo.com");
        nuevo.setRoles(Set.of("ROLE_USER"));
        UsuarioDto creado = usuarioService.crear(nuevo);

        usuarioService.eliminar(creado.getId());
        assertThrows(EntityNotFoundException.class, () -> usuarioService.obtenerPorId(creado.getId()));
    }

    @Test
    void actualizarUsuarioTest() {
        UsuarioDto nuevo = new UsuarioDto();
        nuevo.setUsername("editar");
        nuevo.setPassword("pw");
        nuevo.setFullName("Editar Nombre");
        nuevo.setEmail("edit@correo.com");
        nuevo.setRoles(Set.of("ROLE_USER"));
        UsuarioDto creado = usuarioService.crear(nuevo);

        UsuarioDto update = new UsuarioDto();
        update.setFullName("Nuevo Nombre");
        update.setEmail("nuevo@correo.com");
        update.setRoles(Set.of("ROLE_ADMIN"));

        UsuarioDto actualizado = usuarioService.actualizar(creado.getId(), update);

        assertEquals("Nuevo Nombre", actualizado.getFullName());
        assertEquals("nuevo@correo.com", actualizado.getEmail());
        assertTrue(actualizado.getRoles().contains("ROLE_ADMIN"));
    }

    @Test
    void listarTodosTest() {
        int usuariosAntes = usuarioService.listarTodos().size();

        UsuarioDto nuevo = new UsuarioDto();
        nuevo.setUsername("userx");
        nuevo.setPassword("pw");
        nuevo.setFullName("Usuario X");
        nuevo.setEmail("userx@correo.com");
        nuevo.setRoles(Set.of("ROLE_USER"));
        usuarioService.crear(nuevo);

        int usuariosDespues = usuarioService.listarTodos().size();

        assertTrue(usuariosDespues > usuariosAntes);
    }
}