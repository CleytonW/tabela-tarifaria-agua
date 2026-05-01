package com.cleytonmelo.watertariff.dto;

import com.cleytonmelo.watertariff.model.enums.TipoCategoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CalculoRequest(
        @NotNull(message = "Tipo da categoria é obrigátoria")
        TipoCategoria categoria,
        @NotBlank(message = "Consumo é obrigátorio")
        Integer consumo
        ) {

}
