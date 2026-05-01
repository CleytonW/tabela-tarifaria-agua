package com.cleytonmelo.watertariff.dto;

import java.util.List;

import com.cleytonmelo.watertariff.model.enums.TipoCategoria;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record CategoriaConsumidorRequest(
        @NotNull(message = "Tipo da categoria é obrigátoria")
        TipoCategoria tipo,
        @NotNull(message = "Categoria tem que ter ao menos uma faixa")
        @Valid
        List<FaixaConsumoRequest> faixas
        ) {

}
