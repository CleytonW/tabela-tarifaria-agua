package com.cleytonmelo.watertariff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cleytonmelo.watertariff.model.FaixaConsumo;

@Repository
public interface FaixaConsumoRepository extends JpaRepository<FaixaConsumo, Long> {
    
}
