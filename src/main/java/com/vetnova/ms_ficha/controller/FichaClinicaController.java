package com.vetnova.ms_ficha.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vetnova.ms_ficha.dto.CitaDTO;
import com.vetnova.ms_ficha.dto.FichaClinicaRequestDTO;
import com.vetnova.ms_ficha.dto.FichaClinicaResponseDTO;
import com.vetnova.ms_ficha.service.FichaClinicaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/fichas")
public class FichaClinicaController {

    private final FichaClinicaService service;

    public FichaClinicaController(FichaClinicaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<FichaClinicaResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FichaClinicaResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<FichaClinicaResponseDTO> guardar(
            @Valid @RequestBody FichaClinicaRequestDTO dto) {

        return new ResponseEntity<>(
                service.guardar(dto),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<FichaClinicaResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody FichaClinicaRequestDTO dto) {

        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        service.eliminar(id);

        return ResponseEntity.ok("Ficha clínica eliminada correctamente");
    }

    @GetMapping("/citas")
    public ResponseEntity<List<CitaDTO>> obtenerCitas() {
        return ResponseEntity.ok(service.obtenerCitas());
    }
}