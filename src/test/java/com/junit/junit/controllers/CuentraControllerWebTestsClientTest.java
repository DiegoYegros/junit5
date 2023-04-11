package com.junit.junit.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junit.junit.models.Cuenta;
import com.junit.junit.models.TransaccionDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@Tag("integration_wtc")
public class CuentraControllerWebTestsClientTest {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    void testTransferir() throws JsonProcessingException {
        //Given
        TransaccionDto transaccionDto = new TransaccionDto();
        transaccionDto.setCuentaOrigenId(1L);
        transaccionDto.setCuentaDestinoId(2L);
        transaccionDto.setMonto(new BigDecimal("100"));
        transaccionDto.setBancoId(1L);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Transferencia realizada con éxito");
        response.put("date", LocalDate.now().toString());
        response.put("status", HttpStatus.OK.value());
        response.put("transaccion", transaccionDto);

        //When
        webTestClient.post().uri("/api/cuentas/transferir")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(transaccionDto)
                .exchange()
                //Then
                .expectStatus().isOk()
                .expectBody()

                //Consume With es otra manera de hacerlo, se puede usar con el jsonPath como esta mas abajo.
                .consumeWith(respuesta -> {
                    try {
                        JsonNode json = objectMapper.readTree(respuesta.getResponseBody());
                        assertEquals("Transferencia realizada con éxito", json.path("mensaje").asText());
                        assertTrue(json.path("mensaje").asText().contains("éxit"));
                        assertEquals(1, json.path("transaccion").path("cuentaOrigenId").asLong());
                        assertEquals(2, json.path("transaccion").path("cuentaDestinoId").asLong());
                        assertEquals(100, json.path("transaccion").path("monto").asLong());
                        assertEquals(1, json.path("transaccion").path("bancoId").asLong());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .jsonPath("$.mensaje").isNotEmpty()
                .jsonPath("$.mensaje").value(is("Transferencia realizada con éxito"))
                .jsonPath("$.mensaje").value(valor -> assertTrue(valor.toString().contains("éxit")))
                .jsonPath("$.transaccion.cuentaOrigenId").value(is(1))
                .jsonPath("$.transaccion.cuentaDestinoId").value(is(2))
                .jsonPath("$.transaccion.monto").value(is(100))
                .jsonPath("$.transaccion.bancoId").value(is(1))
                .json(objectMapper.writeValueAsString(response));
    }

    @Test
    @Order(3)
    void testDetalle2() {
        //Given
        Long id = 1L;
        //When
        webTestClient.get().uri("/api/cuentas/{id}", id)
                .exchange()
                //Then
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Cuenta.class)
                .consumeWith(response -> {
                    Cuenta cuenta = response.getResponseBody();
                    assertNotNull(cuenta);
                    assertEquals(1L, cuenta.getId());
                    assertEquals("Andres", cuenta.getPersona());
                    assertEquals(new BigDecimal("900.00"), cuenta.getSaldo());
                });
    }

    @Test
    @Order(4)
    void testDetalle() {
        //Given
        Long id = 1L;
        //When
        webTestClient.get().uri("/api/cuentas/{id}", id)
                .exchange()
                //Then
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").value(is(1))
                .jsonPath("$.persona").value(is("Andres"))
                .jsonPath("$.saldo").value(is(900.00));
    }

    @Test
    @Order(5)
    void testListar() {
        webTestClient.get().uri("/api/cuentas").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Cuenta.class)
                .hasSize(2)
                .consumeWith(response -> {
                    List<Cuenta> cuentas = response.getResponseBody();
                    assertNotNull(cuentas);
                    cuentas.forEach(c -> {
                        assertNotNull(c.getId());
                        assertTrue(c.getId() > 0);
                    });
                });

    }

    @Test
    @Order(6)
    void testListar2() {
        webTestClient.get().uri("/api/cuentas").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].persona").isEqualTo("Andres")
                .jsonPath("$[0].saldo").isEqualTo(900.00)
                .jsonPath("$[1].id").isEqualTo(2)
                .jsonPath("$[1].persona").isEqualTo("John")
                .jsonPath("$[1].saldo").isEqualTo(2100.00)
                .jsonPath("$").isArray()
                .jsonPath("$").value(hasSize(2));
    }

    @Test
    @Order(7)
    void testGuardar() {
        webTestClient.post().uri("/api/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new Cuenta(null, "Juan", new BigDecimal("1000.00")))
                .exchange()
                .expectStatus()
                .isCreated()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.persona").isEqualTo("Juan")
                .jsonPath("$.saldo").isEqualTo(1000.00);
    }
    @Test
    @Order(8)
    void testGuardar2() {
        webTestClient.post().uri("/api/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new Cuenta(null, "Carl", new BigDecimal("1000.00")))
                .exchange()
                .expectStatus()
                .isCreated()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody(Cuenta.class)
                .consumeWith(response -> {
                    Cuenta cuenta = response.getResponseBody();
                    assertNotNull(cuenta);
                    assertNotNull(cuenta.getId());
                    assertEquals("Carl", cuenta.getPersona());
                    assertEquals(new BigDecimal("1000.00"), cuenta.getSaldo());
                });
    }
    @Test
    @Order(9)
    void testEliminar(){
        webTestClient.get().uri("/api/cuentas").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Cuenta.class)
                .hasSize(4);
        webTestClient.delete().uri("/api/cuentas/{id}", 1L)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();
        webTestClient.get().uri("/api/cuentas").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Cuenta.class)
                .hasSize(3);
        webTestClient.get().uri("/api/cuentas/{id}", 1L)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().isEmpty();
    }

}