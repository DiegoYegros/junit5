package com.junit.junit.repositories;
import com.junit.junit.models.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    @Query("SELECT c FROM Cuenta c WHERE c.persona = ?1")
    Optional<Cuenta> findByPersona(String persona);

}