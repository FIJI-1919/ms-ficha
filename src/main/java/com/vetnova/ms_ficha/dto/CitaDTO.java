package com.vetnova.ms_ficha.dto;

import lombok.Data;

@Data
public class CitaDTO {

    private Long id;
    private String mascota;
    private String dueno;
    private String fecha;
    private String hora;
    private String veterinario;
}