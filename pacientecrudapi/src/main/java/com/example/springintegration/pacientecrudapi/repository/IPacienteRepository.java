package com.example.springintegration.pacientecrudapi.repository;

import com.example.springintegration.pacientecrudapi.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface IPacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByDui(String dui);
}