package com.junit.junit.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junit.junit.models.TransaccionDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    }

}
