package com.example.springintegration.pacientecrudapi.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListadoPacientesResponse {
    private List<PacienteResponseDto> pacientes;
    private int totalpacientes;
}