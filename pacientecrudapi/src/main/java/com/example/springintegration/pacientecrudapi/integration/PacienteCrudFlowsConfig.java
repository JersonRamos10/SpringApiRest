package com.example.springintegration.pacientecrudapi.integration;

import com.example.springintegration.pacientecrudapi.model.*;
import com.example.springintegration.pacientecrudapi.service.PacienteService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.json.JsonToObjectTransformer;
import org.springframework.integration.json.ObjectToJsonTransformer;
import org.springframework.messaging.MessageHeaders;

@Configuration
public class PacienteCrudFlowsConfig {

    private final PacienteService pacienteService;

    public PacienteCrudFlowsConfig(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }
    /**
     * Metodo auxiliar para extraer el DUI de forma robusta.
     * Si falla el header estandar, lo busca manualmente en la URL.
     */
    private String extractDui(MessageHeaders headers) {
        //intenta obtenerlo de la forma estándar
        String dui = (String) headers.get("dui");
        //Si es null, lo saca manualmente de la URL
        if (dui == null) {
            try {
                String urlString = (String) headers.get("http_requestUrl");
                if (urlString != null) {

                    if (urlString.endsWith("/")) {
                        urlString = urlString.substring(0, urlString.length() - 1);
                    }
                    dui = urlString.substring(urlString.lastIndexOf('/') + 1);
                    System.out.println("DEBUG: DUI extraído manualmente de URL para operación: " + dui);
                }
            } catch (Exception e) {
                System.err.println("Error extrayendo DUI: " + e.getMessage());
            }
        }
        return dui;
    }

    //Flujo para CREAR Paciente (POST /api/pacientes)
    @Bean
    public IntegrationFlow createPacienteFlow() {
        return IntegrationFlow.from(
                        Http.inboundGateway("/api/pacientes")
                                .requestMapping(m -> m.methods(HttpMethod.POST)
                                        .consumes("application/json")
                                        .produces("application/json"))
                                .statusCodeExpression("200"))
                .transform(new JsonToObjectTransformer(PacienteDto.class))
                .handle(pacienteService, "createPaciente")
                .transform(new ObjectToJsonTransformer())
                .get();
    }

    // Flujo para OBTENER Paciente por DUI (GET /api/pacientes/{dui})
    @Bean
    public IntegrationFlow getPacienteByDuiFlow() {
        return IntegrationFlow.from(
                        Http.inboundGateway("/api/pacientes/{dui}")
                                .requestMapping(m -> m.methods(HttpMethod.GET)
                                        .produces("application/json"))
                                .statusCodeExpression("200"))
                .handle((Object payload, MessageHeaders headers) -> {
                    String dui = extractDui(headers);
                    return pacienteService.getPacienteByDui(dui);
                })
                .transform(new ObjectToJsonTransformer())
                .get();
    }

    // Flujo para OBTENER TODOS los Pacientes (GET /api/pacientes)
    @Bean
    public IntegrationFlow getAllPacientesFlow() {
        return IntegrationFlow.from(
                        Http.inboundGateway("/api/pacientes")
                                .requestMapping(m -> m.methods(HttpMethod.GET)
                                        .produces("application/json"))
                                .statusCodeExpression("200"))
                .handle(pacienteService, "getAllPacientes")
                .transform(new ObjectToJsonTransformer())
                .get();
    }

    // Flujo para ACTUALIZAR Paciente (PUT /api/pacientes/{dui})
    @Bean
    public IntegrationFlow updatePacienteFlow() {
        return IntegrationFlow.from(
                        Http.inboundGateway("/api/pacientes/{dui}")
                                .requestMapping(m -> m.methods(HttpMethod.PUT)
                                        .consumes("application/json")
                                        .produces("application/json"))
                                .statusCodeExpression("200"))
                .transform(new JsonToObjectTransformer(PacienteDto.class))

                .handle((PacienteDto payload, MessageHeaders headers) -> {
                    String duiFromPath = extractDui(headers);
                    return pacienteService.updatePaciente(duiFromPath, payload);
                })
                .transform(new ObjectToJsonTransformer())
                .get();
    }

    // Flujo para ELIMINAR Paciente (DELETE /api/pacientes/{dui})
    @Bean
    public IntegrationFlow deletePacienteFlow() {
        return IntegrationFlow.from(
                        Http.inboundGateway("/api/pacientes/{dui}")
                                .requestMapping(m -> m.methods(HttpMethod.DELETE)
                                        .produces("application/json"))
                                .statusCodeExpression("200"))
                .handle((Object payload, MessageHeaders headers) -> {
                    String dui = extractDui(headers); // Usamos el extractor manual
                    return pacienteService.deletePaciente(dui);
                })
                .transform(new ObjectToJsonTransformer())
                .get();
    }
}