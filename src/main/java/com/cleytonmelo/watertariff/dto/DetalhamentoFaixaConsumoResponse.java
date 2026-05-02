package com.cleytonmelo.watertariff.dto;

import java.math.BigDecimal;

public record DetalhamentoFaixaConsumoResponse(
        FaixaConsumoResponse faixa,
        Integer m3Cobrados,
        BigDecimal valorUnitario,
        BigDecimal subtotal
        ) {

}
