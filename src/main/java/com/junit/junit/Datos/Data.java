package com.junit.junit.Datos;

import com.junit.junit.models.Banco;
import com.junit.junit.models.Cuenta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Data {
    public static Optional<Cuenta> crearCuenta001(){
        return Optional.of(new Cuenta(1L, "Andres Guzman", new BigDecimal("1000.12345")));
    }
    public static Optional<Cuenta> crearCuenta002(){
        return Optional.of(new Cuenta(2L, "John", new BigDecimal("2000")));
    }
    public static Optional<Banco> crearBanco001(){
        return Optional.of(new Banco(1L, "Banco de Bogota", 0));
    }

    public static List<Cuenta> crearListaCuentas() {
        List<Cuenta> cuentas = new ArrayList<>();
        cuentas.add(crearCuenta001().orElseThrow());
        cuentas.add(crearCuenta002().orElseThrow());
        return cuentas;
    }
}