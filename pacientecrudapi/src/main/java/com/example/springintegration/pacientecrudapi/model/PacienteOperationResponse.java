package com.example.springintegration.pacientecrudapi.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacienteOperationResponse {
    private PacienteResponseDto paciente; // Puede ser null en caso de error o eliminación
    private OperationResult result;
}