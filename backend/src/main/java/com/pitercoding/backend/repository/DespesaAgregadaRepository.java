package com.pitercoding.backend.repository;

import com.pitercoding.backend.domain.DespesaAgregada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DespesaAgregadaRepository extends JpaRepository<DespesaAgregada, Long> {
}
