package com.junit.junit;

import com.junit.junit.Datos.Data;
import com.junit.junit.exceptions.DineroInsuficienteException;
import com.junit.junit.models.Banco;
import com.junit.junit.models.Cuenta;
import com.junit.junit.repositories.BancoRepository;
import com.junit.junit.repositories.CuentaRepository;
import com.junit.junit.services.CuentasService;
import com.junit.junit.services.CuentasServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;

@SpringBootTest
class TestingApplicationTests {

	@MockBean
	CuentaRepository cuentaRepository;
	@MockBean
	BancoRepository bancoRepository;
	@Autowired
	CuentasService cuentasService;

	@BeforeEach
	void setUp() {
//		cuentaRepository = mock(CuentaRepository.class);
//		bancoRepository = mock(BancoRepository.class);
//		cuentasService = new CuentasServiceImpl(cuentaRepository, bancoRepository);
	}

	@Test
	void contextLoads() {
		when(cuentaRepository.findById(1L)).thenReturn(Data.crearCuenta001());
		when(cuentaRepository.findById(2L)).thenReturn(Data.crearCuenta002());
		when(bancoRepository.findById(1L)).thenReturn(Data.crearBanco001());
		BigDecimal saldoOrigen = cuentasService.revisarSaldo(1L);
		BigDecimal saldoDestino = cuentasService.revisarSaldo(2L);
		assertEquals("1000.12345", saldoOrigen.toPlainString());
		assertEquals("2000", saldoDestino.toPlainString());
		assertThrows(DineroInsuficienteException.class, () -> {
			cuentasService.transferir(1L, 2L, new BigDecimal("1200"), 1L);
		});
		saldoOrigen = cuentasService.revisarSaldo(1L);
		saldoDestino = cuentasService.revisarSaldo(2L);
		assertEquals("1000.12345", saldoOrigen.toPlainString());
		assertEquals("2000", saldoDestino.toPlainString());
		int total = cuentasService.revisarTotalTransferencias(1L);
		assertEquals(1, total);
		verify(cuentaRepository, times(3)).findById(1L);
		verify(cuentaRepository, times(2)).findById(2L);
		verify(cuentaRepository, never()).save(any(Cuenta.class));
		verify(bancoRepository, times(2)).findById(1L);
		verify(bancoRepository).save(any(Banco.class));

	}
	@Test
	void contextLoads2() {

		when(cuentaRepository.findById(1L)).thenReturn(Data.crearCuenta001());
		when(cuentaRepository.findById(2L)).thenReturn(Data.crearCuenta002());
		when(bancoRepository.findById(1L)).thenReturn(Data.crearBanco001());
		BigDecimal saldoOrigen = cuentasService.revisarSaldo(1L);
		BigDecimal saldoDestino = cuentasService.revisarSaldo(2L);
		assertEquals("1000.12345", saldoOrigen.toPlainString());
		assertEquals("2000", saldoDestino.toPlainString());
		assertThrows(DineroInsuficienteException.class, () -> {
			cuentasService.transferir(1L, 2L, new BigDecimal("1200"), 1L);
		});
		saldoOrigen = cuentasService.revisarSaldo(1L);
		saldoDestino = cuentasService.revisarSaldo(2L);
		assertEquals("1000.12345", saldoOrigen.toPlainString());
		assertEquals("2000", saldoDestino.toPlainString());
		int total = cuentasService.revisarTotalTransferencias(1L);
		assertEquals(1, total);
		verify(cuentaRepository, times(3)).findById(1L);
		verify(cuentaRepository, times(2)).findById(2L);
		verify(cuentaRepository, never()).save(any(Cuenta.class));
		verify(bancoRepository, times(2)).findById(1L);
		verify(bancoRepository).save(any(Banco.class));

		verify(cuentaRepository, never()).findAll();
	}
	@Test
	void contextLoad3(){
		when(cuentaRepository.findById(1L)).thenReturn(Data.crearCuenta001());
		Cuenta cuenta1 = cuentasService.findById(1L);
		Cuenta cuenta2 = cuentasService.findById(1L);
		assertSame(cuenta1, cuenta2);
		assertEquals("Andres Guzman", cuenta1.getPersona());
		assertEquals("Andres Guzman", cuenta2.getPersona());
		verify(cuentaRepository, times(2)).findById(1L);
	}
}
