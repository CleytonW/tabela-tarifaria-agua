package com.cleytonmelo.watertariff.model;

import java.util.ArrayList;
import java.util.List;

import com.cleytonmelo.watertariff.model.enums.TipoCategoria;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tb_categoria_consumidor")
public class CategoriaConsumidor {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private TipoCategoria tipo;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "tabela_tarifaria_id")
    private TabelaTarifaria tabelaTarifaria;

    @OneToMany(mappedBy="categoria", cascade= CascadeType.ALL, orphanRemoval=true)
    private List<FaixaConsumo> faixas = new ArrayList<>();
}
