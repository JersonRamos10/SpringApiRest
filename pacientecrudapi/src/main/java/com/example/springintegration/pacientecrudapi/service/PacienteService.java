package com.example.springintegration.pacientecrudapi.service;

import com.example.springintegration.pacientecrudapi.model.*;
import com.example.springintegration.pacientecrudapi.repository.IPacienteRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PacienteService {

    private final IPacienteRepository pacienteRepository;

    public PacienteService(IPacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    /**
     * Crea un nuevo paciente en la base de datos.
     * @param request Datos del paciente a crear.
     * @return PacienteOperationResponse con el resultado de la operación.
     */
    @Transactional
    public PacienteOperationResponse createPaciente(PacienteDto request) {
        System.out.println("PacienteService: Intentando crear paciente con DUI: " + request.getDui());

        // --- VALIDACIONES ---
        if (request.getDui() == null || request.getDui().isEmpty() || request.getDui().length() != 10) {
            return new PacienteOperationResponse(null, new OperationResult("FAILED", "DUI inválido. Debe tener 10 caracteres."));
        }
        if (request.getPrimerNombre() == null || request.getPrimerNombre().isEmpty()) {
            return new PacienteOperationResponse(null, new OperationResult("FAILED", "Primer nombre no puede ser nulo o vacío."));
        }
        // Validacion del NUEVO CAMPO
        if (request.getTipoSangre() == null || request.getTipoSangre().isEmpty()) {
            return new PacienteOperationResponse(null, new OperationResult("FAILED", "El tipo de sangre es obligatorio."));
        }

        // Mapeo de DTO a Entidad
        Paciente nuevoPaciente = new Paciente();
        nuevoPaciente.setDui(request.getDui());
        nuevoPaciente.setPrimerNombre(request.getPrimerNombre());
        nuevoPaciente.setApellido(request.getApellido());
        nuevoPaciente.setFechaNacimiento(request.getFechaNacimiento());
        nuevoPaciente.setTipoSangre(request.getTipoSangre()); // Guardamos el tipo de sangre

        try {
            // Guardar usando el repositorio correcto
            Paciente pacienteGuardado = pacienteRepository.save(nuevoPaciente);
            System.out.println("PacienteService: Paciente " + pacienteGuardado.getDui() + " creado exitosamente.");
            return new PacienteOperationResponse(new PacienteResponseDto(pacienteGuardado), new OperationResult("SUCCESS", "Paciente creado exitosamente."));
        } catch (DataIntegrityViolationException e) {
            System.err.println("PacienteService: Error al crear paciente - DUI duplicado: " + request.getDui());
            return new PacienteOperationResponse(null, new OperationResult("FAILED", "El DUI '" + request.getDui() + "' ya existe."));
        } catch (Exception e) {
            System.err.println("PacienteService: Error inesperado al crear paciente: " + e.getMessage());
            return new PacienteOperationResponse(null, new OperationResult("ERROR", "Ocurrió un error inesperado al crear al paciente."));
        }
    }

    /**
     * Obtiene un paciente por su DUI.
     * @param dui El DUI del paciente a buscar.
     * @return PacienteResponseDto si se encuentra, o un OperationResult si no.
     */
    @Transactional(readOnly = true)
    public Object getPacienteByDui(String dui) {
        System.out.println("PacienteService: Buscando paciente con DUI: " + dui);
        return pacienteRepository.findByDui(dui) // Usamos pacienteRepository
                .<Object>map(PacienteResponseDto::new)
                .orElseGet(() -> new OperationResult("NOT_FOUND", "Paciente con DUI '" + dui + "' no encontrado."));
    }

    /**
     * Obtiene todos los pacientes registrados.
     * @return ListadoPacientesResponse con la lista de pacientes.
     */
    @Transactional(readOnly = true)
    public ListadoPacientesResponse getAllPacientes() {
        System.out.println("PacienteService: Obteniendo todos los pacientes.");
        List<PacienteResponseDto> pacientes = pacienteRepository.findAll().stream() // Usamos pacienteRepository
                .map(PacienteResponseDto::new)
                .collect(Collectors.toList());
        return new ListadoPacientesResponse(pacientes, pacientes.size());
    }

    /**
     * Actualiza un paciente existente por su DUI.
     * @param dui El DUI del paciente a actualizar (del path).
     * @param request Los nuevos datos del paciente (del cuerpo de la petición).
     * @return PacienteOperationResponse con el resultado de la operación.
     */
    @Transactional
    public PacienteOperationResponse updatePaciente(String dui, PacienteDto request) {
        System.out.println("PacienteService: Intentando actualizar paciente con DUI: " + dui);

        return pacienteRepository.findByDui(dui)
                .map(existingPaciente -> {
                    // Actualizar campos
                    existingPaciente.setPrimerNombre(request.getPrimerNombre());
                    existingPaciente.setApellido(request.getApellido());
                    existingPaciente.setFechaNacimiento(request.getFechaNacimiento());

                    // Actualizamos el NUEVO CAMPO si viene en la petición
                    if (request.getTipoSangre() != null && !request.getTipoSangre().isEmpty()) {
                        existingPaciente.setTipoSangre(request.getTipoSangre());
                    }

                    try {
                        Paciente updatedPaciente = pacienteRepository.save(existingPaciente);
                        System.out.println("PacienteService: Paciente con DUI " + dui + " actualizado exitosamente.");
                        return new PacienteOperationResponse(new PacienteResponseDto(updatedPaciente), new OperationResult("SUCCESS", "Paciente actualizado exitosamente."));
                    } catch (Exception e) {
                        System.err.println("PacienteService: Error al actualizar paciente con DUI " + dui + ": " + e.getMessage());
                        return new PacienteOperationResponse(null, new OperationResult("ERROR", "Ocurrió un error al actualizar al paciente."));
                    }
                })
                .orElseGet(() -> {
                    System.err.println("PacienteService: No se encontró paciente con DUI " + dui + " para actualizar.");
                    return new PacienteOperationResponse(null, new OperationResult("NOT_FOUND", "Paciente con DUI '" + dui + "' no encontrado para actualizar."));
                });
    }

    /**
     * Elimina un paciente por su DUI.
     * @param dui El DUI del paciente a eliminar.
     * @return PacienteOperationResponse con el resultado de la operación.
     */
    @Transactional
    public PacienteOperationResponse deletePaciente(String dui) {
        System.out.println("PacienteService: Intentando eliminar paciente con DUI: " + dui);

        return pacienteRepository.findByDui(dui)
                .map(pacienteToDelete -> {
                    try {
                        pacienteRepository.delete(pacienteToDelete);
                        System.out.println("PacienteService: Paciente con DUI " + dui + " eliminado exitosamente.");
                        return new PacienteOperationResponse(new PacienteResponseDto(pacienteToDelete), new OperationResult("SUCCESS", "Paciente eliminado exitosamente."));
                    } catch (Exception e) {
                        System.err.println("PacienteService: Error al eliminar paciente con DUI " + dui + ": " + e.getMessage());
                        return new PacienteOperationResponse(null, new OperationResult("ERROR", "Ocurrió un error al eliminar al paciente."));
                    }
                })
                .orElseGet(() -> {
                    System.err.println("PacienteService: No se encontró paciente con DUI " + dui + " para eliminar.");
                    return new PacienteOperationResponse(null, new OperationResult("NOT_FOUND", "Paciente con DUI '" + dui + "' no encontrado para eliminar."));
                });
    }
}