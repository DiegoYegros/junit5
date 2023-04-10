package com.junit.junit.models;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {
    @Test
    public void testGettersAndSetters() {
        // Create a new Cuenta instance
        Cuenta cuenta = new Cuenta();

        // Set values using setters
        cuenta.setId(1L);
        cuenta.setPersona("John Doe");
        cuenta.setSaldo(new BigDecimal("1000.00"));

        // Test getters using JUnit assertions
        assertEquals(1L, cuenta.getId());
        assertEquals("John Doe", cuenta.getPersona());
        assertEquals(new BigDecimal("1000.00"), cuenta.getSaldo());
    }

    @Test
    public void testConstructor() {
        // Create a new Cuenta instance using the constructor with arguments
        Cuenta cuenta = new Cuenta(2L, "Jane Doe", new BigDecimal("2000.00"));

        // Test getters using JUnit assertions
        assertEquals(2L, cuenta.getId());
        assertEquals("Jane Doe", cuenta.getPersona());
        assertEquals(new BigDecimal("2000.00"), cuenta.getSaldo());
    }

    @Test
    public void testEqualsAndHashCode() {
        // Create two equal Cuenta instances
        Cuenta cuenta1 = new Cuenta(1L, "John Doe", new BigDecimal("1000.00"));
        Cuenta cuenta2 = new Cuenta(1L, "John Doe", new BigDecimal("1000.00"));

        // Test equality and hashCode using JUnit assertions
        assertTrue(cuenta1.equals(cuenta2));
        assertEquals(cuenta1.hashCode(), cuenta2.hashCode());

        // Test inequality
        Cuenta cuenta3 = new Cuenta(3L, "Jane Doe", new BigDecimal("2000.00"));
        assertFalse(cuenta1.equals(cuenta3));
    }
    @Test
    public void testCredito(){
        //Given
        Cuenta cuenta = new Cuenta(1L, "John Doe", new BigDecimal("1000.00"));
        //When
        cuenta.credito(new BigDecimal("1000.00"));
        //Then
        assertEquals(new BigDecimal("2000.00"), cuenta.getSaldo());
    }
    @Test
    public void testDebito(){
        //Given
        Cuenta cuenta = new Cuenta(1L, "John Doe", new BigDecimal("1000.00"));
        //When
        cuenta.debito(new BigDecimal("1000.00"));
        //Then
        assertEquals(new BigDecimal("0.00"), cuenta.getSaldo());
    }
}