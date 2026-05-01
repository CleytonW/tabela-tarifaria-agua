package com.cleytonmelo.watertariff.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tb_faixa_consumo")
public class FaixaConsumo {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private Integer inicioM3;
    private Integer fimM3;

    private BigDecimal valorUnitario;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private CategoriaConsumidor categoria;
}
