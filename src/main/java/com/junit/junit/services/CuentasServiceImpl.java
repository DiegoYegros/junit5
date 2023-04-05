package com.junit.junit.services;

import com.junit.junit.models.Banco;
import com.junit.junit.models.Cuenta;
import com.junit.junit.repositories.BancoRepository;
import com.junit.junit.repositories.CuentaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CuentasServiceImpl implements CuentasService{
    private CuentaRepository cuentaRepository;
    private BancoRepository bancoRepository;

    public CuentasServiceImpl(CuentaRepository cuentaRepository, BancoRepository bancoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.bancoRepository = bancoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cuenta> findAll() {
        return cuentaRepository.findAll();
    }

    @Override
    @Transactional
    public Cuenta save(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
    }

    @Override
    @Transactional(readOnly = true)
    public Cuenta findById(Long id) {

        return cuentaRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public int revisarTotalTransferencias(Long bancoId) {
       Banco banco = bancoRepository.findById(bancoId).orElseThrow();
       return banco.getTotalTransferencia();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal revisarSaldo(Long cuentaId) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId).orElseThrow();
        return cuenta.getSaldo();
    }

    @Override
    @Transactional
    public void transferir(Long cuentaOrigenId, Long cuentaDestinoId, BigDecimal monto, Long bancoId) {
        Banco banco = bancoRepository.findById(bancoId).orElseThrow();
        int totalTransferencia = banco.getTotalTransferencia();
        banco.setTotalTransferencia(totalTransferencia + 1);
        bancoRepository.save(banco);

        Cuenta cuentaOrigen = cuentaRepository.findById(cuentaOrigenId).orElseThrow();
        cuentaOrigen.debito(monto);
        cuentaRepository.save(cuentaOrigen);
        Cuenta cuentaDestino = cuentaRepository.findById(cuentaDestinoId).orElseThrow();
        cuentaDestino.credito(monto);
        cuentaRepository.save(cuentaDestino);
    }
}
