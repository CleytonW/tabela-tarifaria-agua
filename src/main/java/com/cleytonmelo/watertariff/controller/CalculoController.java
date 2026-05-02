package com.cleytonmelo.watertariff.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cleytonmelo.watertariff.dto.CalculoRequest;
import com.cleytonmelo.watertariff.dto.CalculoResponse;
import com.cleytonmelo.watertariff.service.CalculoTarifaService;

@RestController
@RequestMapping("/api/calculos")
public class CalculoController {

    private final CalculoTarifaService calculoTarifaService;

    public CalculoController(CalculoTarifaService calculoTarifaService) {
        this.calculoTarifaService = calculoTarifaService;
    }

    @PostMapping
    public ResponseEntity<CalculoResponse> calcular(@RequestBody CalculoRequest request) {
        CalculoResponse resposta = calculoTarifaService.calcular(request);

        return ResponseEntity.ok(resposta);
    }

}
