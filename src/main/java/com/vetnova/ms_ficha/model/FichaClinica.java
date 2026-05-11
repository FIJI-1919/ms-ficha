package com.vetnova.ms_ficha.model;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor

public class FichaClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La mascota es obligatoria")
    @Size(min = 2, max = 50)
    private String mascota;

    @NotBlank(message = "El diagnostico es obligatorio")
    @Size(min = 5, max = 200)
    private String diagnostico;

    @NotBlank(message = "El tratamiento es obligatorio")
    @Size(min = 5, max = 200)
    private String tratamiento;

    @NotBlank(message = "El veterinario es obligatorio")
    private String veterinario;
}