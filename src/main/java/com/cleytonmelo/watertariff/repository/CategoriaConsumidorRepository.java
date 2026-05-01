package com.cleytonmelo.watertariff.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cleytonmelo.watertariff.model.CategoriaConsumidor;
import com.cleytonmelo.watertariff.model.enums.TipoCategoria;

@Repository
public interface CategoriaConsumidorRepository extends JpaRepository<CategoriaConsumidor, Long> {
    Optional<CategoriaConsumidor> findByTipo(TipoCategoria tipo);
}
