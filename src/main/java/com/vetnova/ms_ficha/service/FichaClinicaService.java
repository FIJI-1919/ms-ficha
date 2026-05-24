package com.vetnova.ms_ficha.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vetnova.ms_ficha.model.FichaClinica;
import com.vetnova.ms_ficha.repository.FichaClinicaRepository;

@Service
public class FichaClinicaService {

    private final FichaClinicaRepository repository;

    public FichaClinicaService(FichaClinicaRepository repository) {
        this.repository = repository;
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
}