package com.example.springintegration.pacientecrudapi.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacienteResponseDto {
    private Long id;
    private String dui;
    private String primerNombre;
    private String apellido;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    private String tipoSangre;

    // Constructor para convertir de entidad Paciente a DTO
    public PacienteResponseDto(Paciente paciente) {
        this.id = paciente.getId();
        this.dui = paciente.getDui();
        this.primerNombre = paciente.getPrimerNombre();
        this.apellido = paciente.getApellido();
        this.fechaNacimiento = paciente.getFechaNacimiento();
        this.tipoSangre = paciente.getTipoSangre();
    }
}