package com.junit.junit.controllers;
import com.junit.junit.models.Cuenta;
import com.junit.junit.models.TransaccionDto;
import com.junit.junit.services.CuentasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {
    @Autowired
    private CuentasService cuentasService;
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Cuenta detalle(@PathVariable Long id){
        return cuentasService.findById(id);
    }
    @PostMapping("/transferir")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> transferir(@RequestBody TransaccionDto dto){
        cuentasService.transferir(dto.getCuentaOrigenId(), dto.getCuentaDestinoId(), dto.getMonto(), dto.getBancoId());
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Transferencia realizada con éxito");
        response.put("date", LocalDate.now().toString());
        response.put("status", HttpStatus.OK.value());
        response.put("transaccion", dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Cuenta> listar(){
        return cuentasService.findAll();
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cuenta guardar(@RequestBody Cuenta cuenta){
        return cuentasService.save(cuenta);
    }
}
