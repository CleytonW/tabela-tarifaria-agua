package com.cleytonmelo.watertariff.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record FaixaConsumoRequest(
        @NotNull(message = "Faixa inicial obrigátoria")
        @Min(value = 0, message = "Valor da faixa inicial não pode ser negativo")
        Integer inicioM3,
        @NotNull(message = "Faixa final obrigátoria")
        Integer fimM3,
        @NotNull(message = "Valor unitário é obrigátorio")
        @DecimalMin(value = "0.01", message = "Valor unitário não pode ser igual a zero")
        BigDecimal valorUnitario
        ) {

}
