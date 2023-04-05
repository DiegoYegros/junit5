package com.junit.junit.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BancoTest {
    @Test
    public void testGettersAndSetters() {
        // Create a new Banco instance
        Banco banco = new Banco();

        // Set values using setters
        banco.setId(1L);
        banco.setNombre("Bank of Test");
        banco.setTotalTransferencia(10);

        // Test getters using JUnit assertions
        assertEquals(1L, banco.getId());
        assertEquals("Bank of Test", banco.getNombre());
        assertEquals(10, banco.getTotalTransferencia());
    }

    @Test
    public void testConstructor() {
        // Create a new Banco instance using the constructor with arguments
        Banco banco = new Banco(2L, "Bank of Test 2", 20);

        // Test getters using JUnit assertions
        assertEquals(2L, banco.getId());
        assertEquals("Bank of Test 2", banco.getNombre());
        assertEquals(20, banco.getTotalTransferencia());
    }
}