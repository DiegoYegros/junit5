package com.junit.junit.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junit.junit.models.Cuenta;
import com.junit.junit.models.TransaccionDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("integration_rt")
public class CuentaControllerTestRestTemplate {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
    }

    @Test
    @Order(1)
    void testTransferir() throws JsonProcessingException {
        TransaccionDto transaccionDto = new TransaccionDto();
        transaccionDto.setMonto(new BigDecimal("100"));
        transaccionDto.setCuentaOrigenId(1L);
        transaccionDto.setCuentaDestinoId(2L);
        transaccionDto.setBancoId(1L);
        ResponseEntity<String> entity = testRestTemplate.postForEntity("/api/cuentas/transferir", transaccionDto, String.class);
        String json = entity.getBody();
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, entity.getHeaders().getContentType());
        assertNotNull(json);
        assertEquals(true, json.contains("Transferencia realizada con éxito"));
        JsonNode jsonNode = objectMapper.readTree(json);
        assertEquals("Transferencia realizada con éxito", jsonNode.get("mensaje").asText());
        assertEquals("100", jsonNode.get("transaccion").get("monto").asText());
        assertEquals(1, jsonNode.get("transaccion").get("cuentaOrigenId").asLong());
        assertEquals(2, jsonNode.get("transaccion").get("cuentaDestinoId").asLong());
        assertEquals(1, jsonNode.get("transaccion").get("bancoId").asLong());
        assertEquals(LocalDate.now().toString(), jsonNode.get("date").asText());
        Map<String,Object> response2 = new HashMap<>();
        response2.put("mensaje", "Transferencia realizada con éxito");
        response2.put("date", LocalDate.now().toString());
        response2.put("transaccion", transaccionDto);
        response2.put("status", HttpStatus.OK.value());
        assertEquals(json, objectMapper.writeValueAsString(response2));
    }
    @Test
    @Order(2)
    void testDetalle(){
        ResponseEntity<Cuenta> respuesta = testRestTemplate.getForEntity("/api/cuentas/1", Cuenta.class);
        Cuenta cuenta = respuesta.getBody();
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, respuesta.getHeaders().getContentType());
        assertNotNull(cuenta);
        assertEquals(1, cuenta.getId());
        assertEquals("Andres", cuenta.getPersona());
        assertEquals("900.00", cuenta.getSaldo().toPlainString());
    }
    @Test
    @Order(3)
    void testListar() throws JsonProcessingException {
        ResponseEntity<Cuenta[]> respuesta = testRestTemplate.getForEntity("/api/cuentas", Cuenta[].class);
        List<Cuenta> cuentas = Arrays.asList(respuesta.getBody());
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, respuesta.getHeaders().getContentType());
        assertNotNull(cuentas);
        assertEquals(2, cuentas.size());
        assertEquals(1, cuentas.get(0).getId());
        assertEquals("Andres", cuentas.get(0).getPersona());
        assertEquals("900.00", cuentas.get(0).getSaldo().toPlainString());
        assertEquals(2, cuentas.get(1).getId());
        assertEquals("John", cuentas.get(1).getPersona());
        assertEquals("2100.00", cuentas.get(1).getSaldo().toPlainString());
        JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(cuentas));
        assertEquals(1, json.get(0).get("id").asLong());
        assertEquals("Andres", json.get(0).get("persona").asText());
        assertEquals("900.0", json.get(0).get("saldo").asText());
        assertEquals(2, json.get(1).get("id").asLong());
        assertEquals("John", json.get(1).get("persona").asText());
        assertEquals("2100.0", json.get(1).get("saldo").asText());
    }
    @Test
    @Order(4)
    void testGuardar(){
        //Create a save test
        Cuenta cuenta = new Cuenta();
        cuenta.setPersona("Test");
        cuenta.setSaldo(new BigDecimal("1000"));
        ResponseEntity<Cuenta> respuesta = testRestTemplate.postForEntity("/api/cuentas", cuenta, Cuenta.class);
        Cuenta cuentaGuardada = respuesta.getBody();
        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, respuesta.getHeaders().getContentType());
        assertNotNull(cuentaGuardada);
        assertEquals("Test", cuentaGuardada.getPersona());
        assertEquals("1000", cuentaGuardada.getSaldo().toPlainString());
    }
    @Test
    @Order(5)
    void testEliminar(){
        ResponseEntity<Cuenta[]> respuesta = testRestTemplate.getForEntity("/api/cuentas", Cuenta[].class);
        List<Cuenta> cuentas = Arrays.asList(respuesta.getBody());
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, respuesta.getHeaders().getContentType());
        assertNotNull(cuentas);
        assertEquals(3, cuentas.size());
        //Create a delete test
//        testRestTemplate.delete("/api/cuentas/3");
        Map<String, Long> pathVariables = new HashMap<>();
        pathVariables.put("id", 3L);
        ResponseEntity<Void> exchange = testRestTemplate.exchange("/api/cuentas/{id}", HttpMethod.DELETE, null, Void.class, pathVariables);
        assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode());
        assertNull(exchange.getBody());
        respuesta = testRestTemplate.getForEntity("/api/cuentas", Cuenta[].class);
        cuentas = Arrays.asList(Objects.requireNonNull(respuesta.getBody()));
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, respuesta.getHeaders().getContentType());
        assertNotNull(cuentas);
        assertEquals(2, cuentas.size());
    }
}
