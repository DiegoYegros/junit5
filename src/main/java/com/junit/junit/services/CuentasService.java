package com.junit.junit.services;

import com.junit.junit.models.Cuenta;

import java.math.BigDecimal;

public interface CuentasService {
    Cuenta findById(Long id);
    int revisarTotalTransferencias(Long bancoId);
    BigDecimal revisarSaldo(Long cuentaId);
    void transferir(Long cuentaOrigenId, Long cuentaDestinoId, BigDecimal monto, Long bancoId);
}
