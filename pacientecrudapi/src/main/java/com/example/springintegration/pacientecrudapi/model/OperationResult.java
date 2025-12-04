package com.example.springintegration.pacientecrudapi.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationResult {
    private String status; // Ej: "SUCCESS", "FAILED", "ERROR"
    private String message;
}