package com.cleytonmelo.watertariff.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record TabelaTarifariaRequest(
        @NotBlank(message = "Nome da tabela é obrigátorio")
        String nome,
        LocalDate dataVigencia,
        @NotEmpty(message = "Tabela tem que ter no minimo uma categoria de consumidor")
        List<CategoriaConsumidorRequest> categorias) {

}
