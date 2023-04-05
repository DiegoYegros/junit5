package com.junit.junit;

import com.junit.junit.models.Cuenta;
import com.junit.junit.repositories.CuentaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
public class IntegracionJpaTest {
    @Autowired
    CuentaRepository cuentaRepository;
    @Test
    void testFindById(){
        Optional<Cuenta> cuenta = cuentaRepository.findById(1L);
        assertTrue(cuenta.isPresent());
        assertEquals("Andres", cuenta.orElseThrow().getPersona());
        assertEquals("1000.00", cuenta.orElseThrow().getSaldo().toPlainString());
    }
    @Test
    void testFindByPersonaThrowException(){
        Optional<Cuenta> cuenta = cuentaRepository.findById(3L);
        assertTrue(!cuenta.isPresent());
        assertThrows(NoSuchElementException.class, cuenta::orElseThrow);
    }
    @Test
    void testFindAll(){
        List<Cuenta> cuentas = cuentaRepository.findAll();
        assertFalse(cuentas.isEmpty());
        assertEquals(2, cuentas.size());
    }
    @Test
    void saveAll(){
        //Given
        Cuenta cuentaPEepe = new Cuenta(null, "Pepe", new BigDecimal("3000"));
        //When
        Cuenta pepe = cuentaRepository.save(cuentaPEepe);
        //Then
        assertEquals("Pepe", pepe.getPersona());
        assertEquals("3000", pepe.getSaldo().toPlainString());
    }
    @Test
    void update(){
        //Given
        Cuenta cuentaPepe = new Cuenta(1L, "Pepe", new BigDecimal("3000"));
        //When
        Cuenta pepe = cuentaRepository.save(cuentaPepe);
        //Then
        assertEquals("Pepe", pepe.getPersona());
        assertEquals("3000", pepe.getSaldo().toPlainString());
        //When
        pepe.setSaldo(new BigDecimal("4000"));
        Cuenta cuentaActualizada = cuentaRepository.save(pepe);
        //Then
        assertEquals("Pepe", cuentaActualizada.getPersona());
        assertEquals("4000", cuentaActualizada.getSaldo().toPlainString());
    }
    @Test
    void testDelete(){
        Cuenta cuenta = cuentaRepository.findById(1L).orElseThrow();
        assertEquals("Andres", cuenta.getPersona());
        cuentaRepository.delete(cuenta);
        assertThrows(NoSuchElementException.class, ()->
                cuentaRepository.findById(1L).orElseThrow());
        assertFalse(cuentaRepository.findById(1L).isPresent());
    }
}
