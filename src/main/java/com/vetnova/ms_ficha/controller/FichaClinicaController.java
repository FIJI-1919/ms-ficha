package com.vetnova.ms_ficha.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.vetnova.ms_ficha.dto.CitaDTO;
import com.vetnova.ms_ficha.model.FichaClinica;
import com.vetnova.ms_ficha.service.FichaClinicaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/fichas")
public class FichaClinicaController {

    private final FichaClinicaService service;

    public FichaClinicaController(FichaClinicaService service) {
        this.service = service;
    }

    @GetMapping
    public List<FichaClinica> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public FichaClinica buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public FichaClinica guardar(@Valid @RequestBody FichaClinica ficha) {
        return service.guardar(ficha);
    }

    @GetMapping("/citas")
    public List<CitaDTO> obtenerCitas() {
        return service.obtenerCitas();
    }
}