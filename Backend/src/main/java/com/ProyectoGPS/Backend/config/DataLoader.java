package com.ProyectoGPS.Backend.config;

import com.ProyectoGPS.Backend.model.Permiso;
import com.ProyectoGPS.Backend.model.Rol;
import com.ProyectoGPS.Backend.repository.PermisoRepository;
import com.ProyectoGPS.Backend.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final PermisoRepository permisoRepository;
    private final RolRepository rolRepository;

    @Override
    public void run(String... args) throws Exception {
        createPermissionsAndRoles();
    }

    private void createPermissionsAndRoles() {
        log.info("Iniciando carga de datos iniciales...");

        // Crear permisos básicos si no existen
        List<String> permisos = Arrays.asList(
            "LEER_USUARIOS",
            "CREAR_USUARIOS", 
            "EDITAR_USUARIOS",
            "ELIMINAR_USUARIOS",
            "LEER_ROLES",
            "CREAR_ROLES",
            "EDITAR_ROLES",
            "ELIMINAR_ROLES",
            "LEER_INVENTARIO",
            "CREAR_INVENTARIO",
            "EDITAR_INVENTARIO",
            "ELIMINAR_INVENTARIO",
            "GESTIONAR_SISTEMA"
        );

        for (String nombrePermiso : permisos) {
            if (permisoRepository.findByNombre(nombrePermiso).isEmpty()) {
                Permiso permiso = new Permiso();
                permiso.setNombre(nombrePermiso);
                permisoRepository.save(permiso);
                log.info("Permiso creado: {}", nombrePermiso);
            }
        }

        // Crear roles básicos si no existen
        createRoleIfNotExists("ADMIN", Arrays.asList(
            "LEER_USUARIOS", "CREAR_USUARIOS", "EDITAR_USUARIOS", "ELIMINAR_USUARIOS",
            "LEER_ROLES", "CREAR_ROLES", "EDITAR_ROLES", "ELIMINAR_ROLES",
            "LEER_INVENTARIO", "CREAR_INVENTARIO", "EDITAR_INVENTARIO", "ELIMINAR_INVENTARIO",
            "GESTIONAR_SISTEMA"
        ));

        createRoleIfNotExists("USER", Arrays.asList(
            "LEER_USUARIOS", "LEER_INVENTARIO", "CREAR_INVENTARIO", "EDITAR_INVENTARIO"
        ));

        createRoleIfNotExists("VIEWER", Arrays.asList(
            "LEER_USUARIOS", "LEER_INVENTARIO"
        ));

        log.info("Carga de datos iniciales completada.");
    }

    private void createRoleIfNotExists(String nombreRol, List<String> nombresPermisos) {
        if (rolRepository.findByNombre(nombreRol).isEmpty()) {
            Rol rol = new Rol();
            rol.setNombre(nombreRol);
            
            Set<Permiso> permisos = new HashSet<>();
            for (String nombrePermiso : nombresPermisos) {
                permisoRepository.findByNombre(nombrePermiso)
                    .ifPresent(permisos::add);
            }
            rol.setPermisos(permisos);
            
            rolRepository.save(rol);
            log.info("Rol creado: {} con {} permisos", nombreRol, permisos.size());
        }
    }
}