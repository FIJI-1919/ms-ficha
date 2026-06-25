package com.vetnova.ms_ficha.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.vetnova.ms_ficha.dto.FichaClinicaRequestDTO;
import com.vetnova.ms_ficha.dto.FichaClinicaResponseDTO;
import com.vetnova.ms_ficha.exception.FichaNoEncontradaException;
import com.vetnova.ms_ficha.exception.GlobalExceptionHandler;
import com.vetnova.ms_ficha.service.FichaClinicaService;

@ExtendWith(MockitoExtension.class)
class FichaClinicaControllerTest {

    @Mock
    private FichaClinicaService fichaService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        FichaClinicaController controller =
                new FichaClinicaController(fichaService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("GET /api/v1/fichas debe listar fichas")
    void debeListarFichas() throws Exception {

        FichaClinicaResponseDTO ficha =
                new FichaClinicaResponseDTO(
                        1L,
                        1L,
                        1L,
                        "Otitis",
                        "Medicamentos",
                        "Observacion",
                        "Dr. Perez",
                        LocalDate.now()
                );

        when(fichaService.listar())
                .thenReturn(List.of(ficha));

        mockMvc.perform(get("/api/v1/fichas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(fichaService).listar();
    }

    @Test
    @DisplayName("GET /api/v1/fichas/{id} debe buscar ficha")
    void debeBuscarFichaPorId() throws Exception {

        FichaClinicaResponseDTO ficha =
                new FichaClinicaResponseDTO(
                        1L,
                        1L,
                        1L,
                        "Otitis",
                        "Medicamentos",
                        "Observacion",
                        "Dr. Perez",
                        LocalDate.now()
                );

        when(fichaService.buscarPorId(1L))
                .thenReturn(ficha);

        mockMvc.perform(get("/api/v1/fichas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.diagnostico")
                        .value("Otitis"));

        verify(fichaService).buscarPorId(1L);
    }

    @Test
    @DisplayName("GET /api/v1/fichas/{id} retorna 404")
    void debeRetornar404() throws Exception {

        when(fichaService.buscarPorId(99L))
                .thenThrow(
                        new FichaNoEncontradaException(
                                "Ficha clínica no encontrada"
                        )
                );

        mockMvc.perform(get("/api/v1/fichas/99"))
                .andExpect(status().isNotFound());

        verify(fichaService).buscarPorId(99L);
    }

    @Test
    @DisplayName("POST /api/v1/fichas debe guardar ficha")
    void debeGuardarFicha() throws Exception {

        FichaClinicaResponseDTO respuesta =
                new FichaClinicaResponseDTO(
                        1L,
                        1L,
                        1L,
                        "Otitis",
                        "Medicamentos",
                        "Observacion",
                        "Dr. Perez",
                        LocalDate.now()
                );

        when(fichaService.guardar(any(FichaClinicaRequestDTO.class)))
                .thenReturn(respuesta);

        String json = """
                {
                  "citaId":1,
                  "diagnostico":"Otitis",
                  "tratamiento":"Medicamentos",
                  "observaciones":"Observacion",
                  "veterinario":"Dr. Perez"
                }
                """;

        mockMvc.perform(
                post("/api/v1/fichas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        verify(fichaService)
                .guardar(any(FichaClinicaRequestDTO.class));
    }

    @Test
    @DisplayName("DELETE /api/v1/fichas/{id} elimina ficha")
    void debeEliminarFicha() throws Exception {

        mockMvc.perform(delete("/api/v1/fichas/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "Ficha clínica eliminada correctamente"
                ));

        verify(fichaService).eliminar(eq(1L));
    }
}