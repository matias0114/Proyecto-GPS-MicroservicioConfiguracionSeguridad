package com.ProyectoGPS.Backend.Integracion;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.ProyectoGPS.Backend.dto.ParametroDto;
import com.ProyectoGPS.Backend.service.ParametroService;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ParametroTestIntegracion {

    @Autowired
    private ParametroService parametroService;

    @BeforeEach
    void limpiar() {
        // No hace falta limpiar manualmente, @Transactional lo hace automático
    }

    @Test
    void testCrearYObtenerParametro() {
        ParametroDto nuevo = new ParametroDto();
        nuevo.setClave("APP_MODE");
        nuevo.setValor("PROD");
        nuevo.setDescripcion("Modo de la aplicación");

        ParametroDto creado = parametroService.crear(nuevo);

        assertEquals("APP_MODE", creado.getClave());
        assertEquals("PROD", creado.getValor());

        ParametroDto buscado = parametroService.obtenerPorClave("APP_MODE");
        assertEquals("Modo de la aplicación", buscado.getDescripcion());
    }

    @Test
    void testActualizarParametro() {
        ParametroDto nuevo = new ParametroDto();
        nuevo.setClave("LIMITE");
        nuevo.setValor("100");
        nuevo.setDescripcion("Límite de intentos");
        parametroService.crear(nuevo);

        ParametroDto update = new ParametroDto();
        update.setClave("LIMITE");
        update.setValor("200");
        update.setDescripcion("Nuevo límite");

        ParametroDto actualizado = parametroService.actualizar("LIMITE", update);
        assertEquals("200", actualizado.getValor());
        assertEquals("Nuevo límite", actualizado.getDescripcion());
    }

    @Test
    void testEliminarParametro() {
        ParametroDto nuevo = new ParametroDto();
        nuevo.setClave("BORRAR");
        nuevo.setValor("1");
        nuevo.setDescripcion("Se eliminará");
        parametroService.crear(nuevo);

        parametroService.eliminar("BORRAR");
        assertThrows(EntityNotFoundException.class, () -> parametroService.obtenerPorClave("BORRAR"));
    }

    @Test
    void testCrearParametroDuplicadoFalla() {
        ParametroDto nuevo = new ParametroDto();
        nuevo.setClave("DUPLICADO");
        nuevo.setValor("A");
        nuevo.setDescripcion("Original");
        parametroService.crear(nuevo);

        ParametroDto repetido = new ParametroDto();
        repetido.setClave("DUPLICADO");
        repetido.setValor("B");
        repetido.setDescripcion("Intento duplicado");

        assertThrows(EntityExistsException.class, () -> parametroService.crear(repetido));
    }

    @Test
    void testListarTodos() {
        ParametroDto p1 = new ParametroDto();
        p1.setClave("C1"); p1.setValor("V1"); p1.setDescripcion("D1");
        parametroService.crear(p1);

        ParametroDto p2 = new ParametroDto();
        p2.setClave("C2"); p2.setValor("V2"); p2.setDescripcion("D2");
        parametroService.crear(p2);

        List<ParametroDto> todos = parametroService.listarTodos();
        assertTrue(todos.size() >= 2);
        assertTrue(todos.stream().anyMatch(p -> p.getClave().equals("C1")));
        assertTrue(todos.stream().anyMatch(p -> p.getClave().equals("C2")));
    }

    @Test
    void testObtenerParametroNoExiste() {
        assertThrows(EntityNotFoundException.class, () -> parametroService.obtenerPorClave("NO_EXISTE"));
    }
}
