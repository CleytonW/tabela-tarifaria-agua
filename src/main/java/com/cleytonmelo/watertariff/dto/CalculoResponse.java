package com.cleytonmelo.watertariff.dto;

import java.math.BigDecimal;
import java.util.List;

public record CalculoResponse(
        String categoria,
        Integer consumoTotal,
        BigDecimal valorTotal,
        List<DetalhamentoFaixaConsumoResponse> detalhamento
        ) {

}
