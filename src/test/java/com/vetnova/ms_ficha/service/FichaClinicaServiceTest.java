package com.vetnova.ms_ficha.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.web.reactive.function.client.WebClient;

import com.vetnova.ms_ficha.dto.FichaClinicaRequestDTO;
import com.vetnova.ms_ficha.dto.FichaClinicaResponseDTO;
import com.vetnova.ms_ficha.exception.FichaNoEncontradaException;
import com.vetnova.ms_ficha.model.FichaClinica;
import com.vetnova.ms_ficha.repository.FichaClinicaRepository;

@ExtendWith(MockitoExtension.class)
class FichaClinicaServiceTest {

    @Mock
    private FichaClinicaRepository repository;

    @Mock
    private WebClient webClient;

    @InjectMocks
    private FichaClinicaService service;

    @Test
    @DisplayName("Debe listar fichas clínicas")
    void debeListarFichas() {

        FichaClinica ficha = new FichaClinica(
                1L,
                1L,
                1L,
                "Otitis",
                "Medicamentos",
                "Observacion",
                "Dr. Perez",
                LocalDate.now()
        );

        when(repository.findAll())
                .thenReturn(List.of(ficha));

        List<FichaClinicaResponseDTO> resultado =
                service.listar();

        assertEquals(1, resultado.size());
        assertEquals("Otitis",
                resultado.get(0).getDiagnostico());

        verify(repository).findAll();
    }

    @Test
    @DisplayName("Debe buscar ficha por ID")
    void debeBuscarFichaPorId() {

        FichaClinica ficha = new FichaClinica(
                1L,
                1L,
                1L,
                "Otitis",
                "Medicamentos",
                "Observacion",
                "Dr. Perez",
                LocalDate.now()
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(ficha));

        FichaClinicaResponseDTO resultado =
                service.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
        assertEquals("Otitis",
                resultado.getDiagnostico());

        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando ficha no existe")
    void debeLanzarExcepcionSiNoExiste() {

        when(repository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                FichaNoEncontradaException.class,
                () -> service.buscarPorId(99L)
        );

        verify(repository).findById(99L);
    }

    @Test
    @DisplayName("Debe eliminar una ficha existente")
    void debeEliminarFicha() {

        FichaClinica ficha = new FichaClinica(
                1L,
                1L,
                1L,
                "Otitis",
                "Medicamentos",
                "Observacion",
                "Dr. Perez",
                LocalDate.now()
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(ficha));

        service.eliminar(1L);

        verify(repository).findById(1L);
        verify(repository).delete(ficha);
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar ficha inexistente")
    void debeLanzarExcepcionAlEliminar() {

        when(repository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                FichaNoEncontradaException.class,
                () -> service.eliminar(99L)
        );

        verify(repository).findById(99L);
    }
}