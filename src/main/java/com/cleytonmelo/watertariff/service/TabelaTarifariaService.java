package com.cleytonmelo.watertariff.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

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

            List<FaixaConsumo> faixas = catReq.faixas().stream().map(faixaReq -> {

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
