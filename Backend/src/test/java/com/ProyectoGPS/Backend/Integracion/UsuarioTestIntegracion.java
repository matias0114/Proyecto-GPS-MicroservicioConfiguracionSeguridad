package com.ProyectoGPS.Backend.Integracion;

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
class UsuarioTestIntegracion {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        // Crea un rol base en la BD de test antes de cada prueba
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
    void testCrearYObtenerUsuario() {
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

        // Obtener por ID
        UsuarioDto buscado = usuarioService.obtenerPorId(creado.getId());
        assertEquals("matias", buscado.getUsername());
    }

    @Test
    void testActualizarUsuario() {
        UsuarioDto nuevo = new UsuarioDto();
        nuevo.setUsername("alguien");
        nuevo.setPassword("1234");
        nuevo.setFullName("Alguien Uno");
        nuevo.setEmail("alguien@correo.com");
        nuevo.setRoles(Set.of("ROLE_USER"));
        UsuarioDto creado = usuarioService.crear(nuevo);

        UsuarioDto update = new UsuarioDto();
        update.setFullName("Alguien Editado");
        update.setEmail("nuevo@correo.com");
        update.setRoles(Set.of("ROLE_ADMIN"));

        UsuarioDto actualizado = usuarioService.actualizar(creado.getId(), update);

        assertEquals("Alguien Editado", actualizado.getFullName());
        assertEquals("nuevo@correo.com", actualizado.getEmail());
        assertTrue(actualizado.getRoles().contains("ROLE_ADMIN"));
    }

    @Test
    void testEliminarUsuario() {
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
    void testListarTodos() {
        int antes = usuarioService.listarTodos().size();

        UsuarioDto nuevo = new UsuarioDto();
        nuevo.setUsername("nuevo");
        nuevo.setPassword("pw");
        nuevo.setFullName("Nuevo Usuario");
        nuevo.setEmail("nuevo@correo.com");
        nuevo.setRoles(Set.of("ROLE_USER"));
        usuarioService.crear(nuevo);

        int despues = usuarioService.listarTodos().size();

        assertTrue(despues > antes);
    }

    @Test
    void testObtenerUsuarioNoExiste() {
        assertThrows(EntityNotFoundException.class, () -> usuarioService.obtenerPorId(9999L));
    }

    @Test
    void testSignupUsuario() {
        UsuarioDto signup = new UsuarioDto();
        signup.setUsername("signup");
        signup.setPassword("pass");
        signup.setFullName("Signup User");
        signup.setEmail("signup@correo.com");
        signup.setRoles(Set.of("ROLE_USER"));
        UsuarioDto creado = usuarioService.signup(signup);

        assertNotNull(creado.getId());
        assertEquals("signup", creado.getUsername());
    }
}