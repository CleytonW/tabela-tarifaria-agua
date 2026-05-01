package com.cleytonmelo.watertariff.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tb_tabela_tarifaria")
public class TabelaTarifaria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private LocalDate dataVigencia; 

    @OneToMany(mappedBy = "tabelaTarifaria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoriaConsumidor> categorias = new ArrayList<>();
}
