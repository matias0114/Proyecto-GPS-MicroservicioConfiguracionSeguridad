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

import com.ProyectoGPS.Backend.dto.ParametroDto;
import com.ProyectoGPS.Backend.model.Parametro;
import com.ProyectoGPS.Backend.repository.ParametroRepository;
import com.ProyectoGPS.Backend.service.Impl.ParametroServiceImpl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

class ParametroServiceTest {

    @Mock
    private ParametroRepository parametroRepo;

    @InjectMocks
    private ParametroServiceImpl parametroService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private ParametroDto getDto() {
        ParametroDto dto = new ParametroDto();
        dto.setClave("TEST_KEY");
        dto.setValor("VALOR123");
        dto.setDescripcion("Descripción de prueba");
        return dto;
    }

    private Parametro getEntity() {
        Parametro p = new Parametro();
        p.setClave("TEST_KEY");
        p.setValor("VALOR123");
        p.setDescripcion("Descripción de prueba");
        return p;
    }

    @Test
    void testCrear_ok() {
        ParametroDto dto = getDto();

        when(parametroRepo.existsById("TEST_KEY")).thenReturn(false);
        when(parametroRepo.save(any(Parametro.class))).thenReturn(getEntity());

        ParametroDto creado = parametroService.crear(dto);

        assertEquals("TEST_KEY", creado.getClave());
        assertEquals("VALOR123", creado.getValor());
        verify(parametroRepo, times(1)).save(any(Parametro.class));
    }

    @Test
    void testCrear_parametroYaExiste() {
        ParametroDto dto = getDto();
        when(parametroRepo.existsById("TEST_KEY")).thenReturn(true);

        assertThrows(EntityExistsException.class, () -> parametroService.crear(dto));
        verify(parametroRepo, never()).save(any());
    }

    @Test
    void testListarTodos() {
        Parametro p1 = new Parametro();
        p1.setClave("K1"); p1.setValor("V1"); p1.setDescripcion("D1");
        Parametro p2 = new Parametro();
        p2.setClave("K2"); p2.setValor("V2"); p2.setDescripcion("D2");

        when(parametroRepo.findAll()).thenReturn(List.of(p1, p2));

        List<ParametroDto> lista = parametroService.listarTodos();

        assertEquals(2, lista.size());
        assertTrue(lista.stream().anyMatch(dto -> dto.getClave().equals("K1")));
        assertTrue(lista.stream().anyMatch(dto -> dto.getClave().equals("K2")));
    }

    @Test
    void testObtenerPorClave_ok() {
        Parametro p = getEntity();
        when(parametroRepo.findById("TEST_KEY")).thenReturn(Optional.of(p));

        ParametroDto dto = parametroService.obtenerPorClave("TEST_KEY");

        assertEquals("VALOR123", dto.getValor());
        assertEquals("Descripción de prueba", dto.getDescripcion());
    }

    @Test
    void testObtenerPorClave_noExiste() {
        when(parametroRepo.findById("NO_KEY")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> parametroService.obtenerPorClave("NO_KEY"));
    }

    @Test
    void testActualizar_ok() {
        Parametro existente = getEntity();
        ParametroDto update = new ParametroDto();
        update.setValor("NUEVO");
        update.setDescripcion("DESC NUEVA");
        update.setClave("TEST_KEY");

        when(parametroRepo.findById("TEST_KEY")).thenReturn(Optional.of(existente));
        Parametro actualizado = new Parametro();
        actualizado.setClave("TEST_KEY");
        actualizado.setValor("NUEVO");
        actualizado.setDescripcion("DESC NUEVA");
        when(parametroRepo.save(any(Parametro.class))).thenReturn(actualizado);

        ParametroDto result = parametroService.actualizar("TEST_KEY", update);

        assertEquals("NUEVO", result.getValor());
        assertEquals("DESC NUEVA", result.getDescripcion());
        verify(parametroRepo, times(1)).save(any(Parametro.class));
    }

    @Test
    void testActualizar_noExiste() {
        ParametroDto update = getDto();
        when(parametroRepo.findById("NOT_FOUND")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> parametroService.actualizar("NOT_FOUND", update));
    }

    @Test
    void testEliminar_ok() {
        when(parametroRepo.existsById("TEST_KEY")).thenReturn(true);
        parametroService.eliminar("TEST_KEY");
        verify(parametroRepo, times(1)).deleteById("TEST_KEY");
    }

    @Test
    void testEliminar_noExiste() {
        when(parametroRepo.existsById("NO_KEY")).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> parametroService.eliminar("NO_KEY"));
    }
}