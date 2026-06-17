package com.vetnova.ms_ficha.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vetnova.ms_ficha.model.FichaClinica;

@Repository
public interface FichaClinicaRepository extends JpaRepository<FichaClinica, Long> {

    Optional<FichaClinica> findByCitaId(Long citaId);
}