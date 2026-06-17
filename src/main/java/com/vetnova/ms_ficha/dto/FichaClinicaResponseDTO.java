package com.vetnova.ms_ficha.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FichaClinicaResponseDTO {

    private Long id;
    private Long citaId;
    private Long mascotaId;
    private String diagnostico;
    private String tratamiento;
    private String observaciones;
    private String veterinario;
    private LocalDate fechaRegistro;
}