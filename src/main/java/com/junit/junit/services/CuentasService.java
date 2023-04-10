package com.junit.junit.services;

import com.junit.junit.models.Cuenta;

import java.math.BigDecimal;
import java.util.List;

public interface CuentasService {
    List<Cuenta> findAll();
    Cuenta save(Cuenta cuenta);
    Cuenta findById(Long id);
    int revisarTotalTransferencias(Long bancoId);
    BigDecimal revisarSaldo(Long cuentaId);
    void transferir(Long cuentaOrigenId, Long cuentaDestinoId, BigDecimal monto, Long bancoId);
    void deleteById(Long id);
}
