package com.cleytonmelo.watertariff.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cleytonmelo.watertariff.dto.FaixaConsumoRequest;
import com.cleytonmelo.watertariff.dto.TabelaTarifariaRequest;
import com.cleytonmelo.watertariff.exception.RegraNegocioException;
import com.cleytonmelo.watertariff.model.CategoriaConsumidor;
import com.cleytonmelo.watertariff.model.FaixaConsumo;
import com.cleytonmelo.watertariff.model.TabelaTarifaria;
import com.cleytonmelo.watertariff.repository.TabelaTarifariaRepository;

@Service
public class TabelaTarifariaService {
    
    private final TabelaTarifariaRepository tabelaRepository;

    public TabelaTarifariaService(TabelaTarifariaRepository tabelaRepository) {
        this.tabelaRepository = tabelaRepository;
    }

    public TabelaTarifaria criar(TabelaTarifariaRequest request) {

        TabelaTarifaria tabela = new TabelaTarifaria();
        tabela.setNome(request.nome());
        tabela.setDataVigencia(request.dataVigencia());


        List<CategoriaConsumidor> categorias = request.categorias().stream()
        .map(catReq -> {

            CategoriaConsumidor categoria = new CategoriaConsumidor();
            categoria.setTipo(catReq.tipo());
            categoria.setTabelaTarifaria(tabela);

            List<FaixaConsumoRequest> faixasRequestOrdenadas = new ArrayList<>(catReq.faixas());
            faixasRequestOrdenadas.sort(Comparator.comparing(faixa -> faixa.inicioM3()));
            
            if (faixasRequestOrdenadas.isEmpty() || faixasRequestOrdenadas.get(0).inicioM3() != 0) {
                throw new RegraNegocioException("A primeira faixa de consumo da categoria " + catReq.tipo() + " deve iniciar em 0 m³.");
            }
            
            for (int i = 1; i < faixasRequestOrdenadas.size(); i++) {
                FaixaConsumoRequest faixaAtual = faixasRequestOrdenadas.get(i);
                FaixaConsumoRequest faixaAnterior = faixasRequestOrdenadas.get(i - 1);
                
                if (faixaAtual.inicioM3() != faixaAnterior.fimM3() + 1) {
                    throw new RegraNegocioException("As faixas da categoria " + catReq.tipo() 
                        + " possuem sobreposição. A faixa que inicia em " + faixaAtual.inicioM3() 
                        + " deveria iniciar em " + (faixaAnterior.fimM3() + 1) + ".");
                }
            }

            List<FaixaConsumo> faixas = faixasRequestOrdenadas.stream().map(faixaReq -> {
                
                if (faixaReq.inicioM3() > faixaReq.fimM3()) {
                    throw new RegraNegocioException("O início não pode ser maior que o fim da faixa.");
                }

                FaixaConsumo faixa = new FaixaConsumo();
                faixa.setInicioM3(faixaReq.inicioM3());
                faixa.setFimM3(faixaReq.fimM3());
                faixa.setValorUnitario(faixaReq.valorUnitario());
                faixa.setCategoria(categoria);

                return faixa;
            }).collect(Collectors.toList());

            categoria.setFaixas(faixas);
            return categoria;

        }).collect(Collectors.toList());

        tabela.setCategorias(categorias);

        return tabelaRepository.save(tabela);
    }
}
