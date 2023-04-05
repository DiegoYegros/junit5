package com.junit.junit.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junit.junit.Datos.Data;
import com.junit.junit.models.Cuenta;
import com.junit.junit.models.TransaccionDto;
import com.junit.junit.services.CuentasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CuentaController.class)
class CuentaControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CuentasService cuentasService;
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void detalle() throws Exception {
        //Given
        when(cuentasService.findById(1L)).thenReturn(Data.crearCuenta001().orElseThrow());
        //When
        mockMvc.perform(get("/api/cuentas/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.persona").value("Andres Guzman"))
                .andExpect(jsonPath("$.saldo").value("1000.12345"));
        verify(cuentasService).findById(1L);
    }
    @Test
    void testTransferir() throws Exception {
        //Given
        TransaccionDto transaccionDto = new TransaccionDto();
        transaccionDto.setCuentaDestinoId(2L);
        transaccionDto.setCuentaOrigenId(1L);
        transaccionDto.setMonto(new BigDecimal("500"));
        transaccionDto.setBancoId(1L);
        //When
        mockMvc.perform(post("/api/cuentas/transferir")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transaccionDto)))
        //Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensaje").value("Transferencia realizada con éxito"))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.transaccion.cuentaOrigenId").value(1L))
                .andExpect(jsonPath("$.transaccion.cuentaDestinoId").value(2L))
                .andExpect(jsonPath("$.transaccion.monto").value("500"))
                .andExpect(jsonPath("$.transaccion.bancoId").value(1L));
        verify(cuentasService).transferir(1L, 2L, new BigDecimal("500"), 1L);
    }
    @Test
    void testListar() throws Exception {
        //Given
        List<Cuenta> cuentas = Data.crearListaCuentas();
        when(cuentasService.findAll()).thenReturn(cuentas);
        //When
        mockMvc.perform(get("/api/cuentas").contentType(MediaType.APPLICATION_JSON))
        //Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].persona").value("Andres Guzman"))
                .andExpect(jsonPath("$[0].saldo").value("1000.12345"))
                .andExpect(jsonPath("$[1].persona").value("John"))
                .andExpect(jsonPath("$[1].saldo").value("2000"))
                .andExpect(content().json(objectMapper.writeValueAsString(cuentas)));
        //Then
    }
    @Test
    void testGuardar() throws Exception {
        //Given
        Cuenta cuenta = Data.crearCuenta001().orElseThrow();
        when(cuentasService.save(any())).then(invocation -> {
            Cuenta c = invocation.getArgument(0);
            c.setId(3L);
            return c;
        });
        //When
        mockMvc.perform(post("/api/cuentas")
        //Then
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cuenta)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.persona").value("Andres Guzman"))
                .andExpect(jsonPath("$.saldo").value("1000.12345"))
                .andExpect(jsonPath("$.id", is(3)));
        verify(cuentasService).save(any());
    }
}