package com.vetnova.ms_ficha.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.vetnova.ms_ficha.dto.CitaDTO;
import com.vetnova.ms_ficha.dto.FichaClinicaRequestDTO;
import com.vetnova.ms_ficha.dto.FichaClinicaResponseDTO;
import com.vetnova.ms_ficha.exception.CitaNoEncontradaException;
import com.vetnova.ms_ficha.exception.ErrorComunicacionException;
import com.vetnova.ms_ficha.exception.FichaNoEncontradaException;
import com.vetnova.ms_ficha.exception.ReglaNegocioException;
import com.vetnova.ms_ficha.model.FichaClinica;
import com.vetnova.ms_ficha.repository.FichaClinicaRepository;

@Service
public class FichaClinicaService {

    private static final Logger logger = LoggerFactory.getLogger(FichaClinicaService.class);

    private final FichaClinicaRepository repository;
    private final WebClient webClient;

    public FichaClinicaService(
            FichaClinicaRepository repository,
            WebClient webClient) {

        this.repository = repository;
        this.webClient = webClient;
    }

    public List<FichaClinicaResponseDTO> listar() {
        logger.info("Listando fichas clínicas");

        return repository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    public FichaClinicaResponseDTO buscarPorId(Long id) {
        logger.info("Buscando ficha clínica con ID: " + id);

        FichaClinica ficha = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Ficha clínica no encontrada con ID: " + id);
                    return new FichaNoEncontradaException(
                            "Ficha clínica no encontrada");
                });

        return convertirAResponse(ficha);
    }

    public FichaClinicaResponseDTO guardar(FichaClinicaRequestDTO dto) {
        logger.info("Registrando ficha clínica para cita ID: " + dto.getCitaId());

        validarFichaDuplicada(dto.getCitaId());

        CitaDTO cita = obtenerCitaPorId(dto.getCitaId());

        validarEstadoCita(cita);

        FichaClinica ficha = new FichaClinica();

        ficha.setCitaId(cita.getId());
        ficha.setMascotaId(cita.getMascotaId());
        ficha.setDiagnostico(dto.getDiagnostico());
        ficha.setTratamiento(dto.getTratamiento());
        ficha.setObservaciones(dto.getObservaciones());
        ficha.setVeterinario(dto.getVeterinario());

        FichaClinica fichaGuardada = repository.save(ficha);

        logger.info("Ficha clínica registrada con ID: " + fichaGuardada.getId());

        return convertirAResponse(fichaGuardada);
    }

    public FichaClinicaResponseDTO actualizar(Long id, FichaClinicaRequestDTO dto) {
        logger.info("Actualizando ficha clínica con ID: " + id);

        FichaClinica ficha = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Ficha clínica no encontrada con ID: " + id);
                    return new FichaNoEncontradaException(
                            "Ficha clínica no encontrada");
                });

        validarCambioCitaEnActualizacion(id, dto.getCitaId());

        CitaDTO cita = obtenerCitaPorId(dto.getCitaId());

        validarEstadoCita(cita);

        ficha.setCitaId(cita.getId());
        ficha.setMascotaId(cita.getMascotaId());
        ficha.setDiagnostico(dto.getDiagnostico());
        ficha.setTratamiento(dto.getTratamiento());
        ficha.setObservaciones(dto.getObservaciones());
        ficha.setVeterinario(dto.getVeterinario());

        FichaClinica fichaActualizada = repository.save(ficha);

        logger.info("Ficha clínica actualizada con ID: " + fichaActualizada.getId());

        return convertirAResponse(fichaActualizada);
    }

    public void eliminar(Long id) {
        logger.info("Eliminando ficha clínica con ID: " + id);

        FichaClinica ficha = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Ficha clínica no encontrada con ID: " + id);
                    return new FichaNoEncontradaException(
                            "Ficha clínica no encontrada");
                });

        repository.delete(ficha);

        logger.info("Ficha clínica eliminada con ID: " + id);
    }

    public List<CitaDTO> obtenerCitas() {
        try {
            logger.info("Consultando citas desde ms-agenda");

            return webClient.get()
                    .uri("http://localhost:8085/api/v1/citas")
                    .retrieve()
                    .bodyToFlux(CitaDTO.class)
                    .collectList()
                    .block();

        } catch (Exception e) {
            logger.error("Error al comunicarse con ms-agenda");

            throw new ErrorComunicacionException(
                    "No se pudieron obtener las citas. Verifica que ms-agenda esté funcionando");
        }
    }

    private CitaDTO obtenerCitaPorId(Long citaId) {
        try {
            logger.info("Validando cita con ID: " + citaId);

            return webClient.get()
                    .uri("http://localhost:8085/api/v1/citas/" + citaId)
                    .retrieve()
                    .bodyToMono(CitaDTO.class)
                    .block();

        } catch (WebClientResponseException.NotFound e) {
            logger.error("Cita no encontrada con ID: " + citaId);

            throw new CitaNoEncontradaException(
                    "La cita con ID " + citaId + " no existe");

        } catch (Exception e) {
            logger.error("Error al comunicarse con ms-agenda");

            throw new ErrorComunicacionException(
                    "No se pudo validar la cita. Verifica que ms-agenda esté funcionando");
        }
    }

    private void validarFichaDuplicada(Long citaId) {
        repository.findByCitaId(citaId)
                .ifPresent(ficha -> {
                    logger.error("Ya existe una ficha clínica para la cita ID: " + citaId);

                    throw new ReglaNegocioException(
                            "Ya existe una ficha clínica registrada para esta cita");
                });
    }

    private void validarCambioCitaEnActualizacion(Long fichaId, Long citaId) {
        repository.findByCitaId(citaId)
                .ifPresent(fichaExistente -> {
                    if (!fichaExistente.getId().equals(fichaId)) {
                        logger.error("La cita ID " + citaId
                                + " ya está asociada a otra ficha clínica");

                        throw new ReglaNegocioException(
                                "La cita indicada ya está asociada a otra ficha clínica");
                    }
                });
    }

    private void validarEstadoCita(CitaDTO cita) {
        if (cita.getEstado() != null
                && cita.getEstado().equalsIgnoreCase("CANCELADA")) {

            logger.error("No se puede crear ficha para cita cancelada ID: "
                    + cita.getId());

            throw new ReglaNegocioException(
                    "No se puede crear una ficha clínica para una cita cancelada");
        }
    }

    private FichaClinicaResponseDTO convertirAResponse(FichaClinica ficha) {
        return new FichaClinicaResponseDTO(
                ficha.getId(),
                ficha.getCitaId(),
                ficha.getMascotaId(),
                ficha.getDiagnostico(),
                ficha.getTratamiento(),
                ficha.getObservaciones(),
                ficha.getVeterinario(),
                ficha.getFechaRegistro());
    }
}