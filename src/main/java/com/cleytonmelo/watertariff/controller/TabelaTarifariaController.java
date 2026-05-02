package com.cleytonmelo.watertariff.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cleytonmelo.watertariff.dto.TabelaTarifariaRequest;
import com.cleytonmelo.watertariff.model.TabelaTarifaria;
import com.cleytonmelo.watertariff.repository.TabelaTarifariaRepository;
import com.cleytonmelo.watertariff.service.TabelaTarifariaService;

@RestController
@RequestMapping("/api/tabela-tarifarias")
public class TabelaTarifariaController {
    private final TabelaTarifariaService tabelaTarifariaService;
    private final TabelaTarifariaRepository repository;

    public TabelaTarifariaController(TabelaTarifariaService tabelaTarifariaService, TabelaTarifariaRepository repository) {
        this.tabelaTarifariaService = tabelaTarifariaService;
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<TabelaTarifaria> criar(@RequestBody TabelaTarifariaRequest request) {
        TabelaTarifaria novaTabela = tabelaTarifariaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaTabela);
    }

    public ResponseEntity<List<TabelaTarifaria>> listar() {
        return ResponseEntity.ok(repository.findAll());
    }

    public ResponseEntity<Void> excluir(@PathVariable long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
