package com.vetnova.ms_ficha.service;

import java.util.List;

import org.springframework.stereotype.Service;

import org.springframework.web.reactive.function.client.WebClient;

import com.vetnova.ms_ficha.dto.CitaDTO;
import com.vetnova.ms_ficha.model.FichaClinica;
import com.vetnova.ms_ficha.repository.FichaClinicaRepository;

@Service
public class FichaClinicaService {

    private final FichaClinicaRepository repository;
    private final WebClient webClient;

    public FichaClinicaService(
            FichaClinicaRepository repository,
            WebClient webClient) {

        this.repository = repository;
        this.webClient = webClient;
    }

    public List<FichaClinica> listar() {
        return repository.findAll();
    }

    public FichaClinica guardar(FichaClinica ficha) {
        return repository.save(ficha);
    }

    public FichaClinica buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<CitaDTO> obtenerCitas() {

        return webClient.get()
                .uri("http://localhost:8085/api/v1/citas")
                .retrieve()
                .bodyToFlux(CitaDTO.class)
                .collectList()
                .block();
    }
}