package com.vetnova.ms_ficha.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class CitaDTO {

    private Long id;
    private Long mascotaId;
    private LocalDate fecha;
    private LocalTime hora;
    private String veterinario;
    private String motivo;
    private String estado;
}