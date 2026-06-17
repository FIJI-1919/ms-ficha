package com.vetnova.ms_ficha.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class FichaClinicaRequestDTO {

    @NotNull(message = "El ID de la cita es obligatorio")
    private Long citaId;

    @NotBlank(message = "El diagnóstico es obligatorio")
    @Size(min = 5, max = 200, message = "El diagnóstico debe tener entre 5 y 200 caracteres")
    private String diagnostico;

    @NotBlank(message = "El tratamiento es obligatorio")
    @Size(min = 5, max = 200, message = "El tratamiento debe tener entre 5 y 200 caracteres")
    private String tratamiento;

    @Size(max = 300, message = "Las observaciones no pueden superar los 300 caracteres")
    private String observaciones;

    @NotBlank(message = "El veterinario es obligatorio")
    @Size(min = 2, max = 50, message = "El veterinario debe tener entre 2 y 50 caracteres")
    private String veterinario;
}