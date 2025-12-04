package com.example.springintegration.pacientecrudapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicacion Spring Boot para la API CRUD de pacientes.
 * Esta clase es el punto de entrada que inicia el contexto de Spring.
 */
@SpringBootApplication
public class PacienteCrudApiApplication {

    public static void main(String[] args) {

        SpringApplication.run(PacienteCrudApiApplication.class, args);
    }

}