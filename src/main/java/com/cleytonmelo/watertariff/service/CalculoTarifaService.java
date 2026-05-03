package com.cleytonmelo.watertariff.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cleytonmelo.watertariff.dto.CalculoRequest;
import com.cleytonmelo.watertariff.dto.CalculoResponse;
import com.cleytonmelo.watertariff.dto.DetalhamentoFaixaConsumoResponse;
import com.cleytonmelo.watertariff.dto.FaixaConsumoResponse;
import com.cleytonmelo.watertariff.exception.RecursoNaoEncontradoException;
import com.cleytonmelo.watertariff.exception.RegraNegocioException;
import com.cleytonmelo.watertariff.model.CategoriaConsumidor;
import com.cleytonmelo.watertariff.model.FaixaConsumo;
import com.cleytonmelo.watertariff.repository.CategoriaConsumidorRepository;

@Service
public class CalculoTarifaService {

    private final CategoriaConsumidorRepository categoriaRepository;

    public CalculoTarifaService(CategoriaConsumidorRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public CalculoResponse calcular(CalculoRequest request) {

        CategoriaConsumidor categoria = categoriaRepository.findByTipo(request.categoria()).orElseThrow(() -> new RecursoNaoEncontradoException("Nenhuma tabela tarifária cadastrada para a categoria: " + request.categoria()));

        List<FaixaConsumo> faixasOrdenadas = categoria.getFaixas().stream().sorted(Comparator.comparing(FaixaConsumo::getInicioM3)).toList();

        int consumoRestante = request.consumo();

        BigDecimal valorTotal = BigDecimal.ZERO;

        List<DetalhamentoFaixaConsumoResponse> detalhamento = new ArrayList<>();

        for (FaixaConsumo faixa : faixasOrdenadas) {

            if (consumoRestante <= 0) {
                break;
            }

            int tamanhoDaFaixa = (faixa.getFimM3() - faixa.getInicioM3());

            int m3Cobrados = Math.min(consumoRestante, tamanhoDaFaixa);

            BigDecimal subtotal = faixa.getValorUnitario().multiply(BigDecimal.valueOf(m3Cobrados));

            valorTotal = valorTotal.add(subtotal);
            consumoRestante -= m3Cobrados;

            detalhamento.add(new DetalhamentoFaixaConsumoResponse(new FaixaConsumoResponse(faixa.getInicioM3(), faixa.getFimM3()), m3Cobrados, faixa.getValorUnitario(), subtotal));
        }

        if (consumoRestante > 0) {
            throw new RegraNegocioException("As faixas cadastradas não cobrem o consumo informado de " + request.consumo() + " m³");
        }

        return new CalculoResponse(categoria.getTipo().name(), request.consumo(), valorTotal, detalhamento);
    }
}
