package com.pitercoding.backend.repository;

import com.pitercoding.backend.domain.DespesaConsolidada;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DespesaConsolidadaRepository extends JpaRepository<DespesaConsolidada, Long> {

    List<DespesaConsolidada> findByRegistroAnsOrderByAnoAscTrimestreAsc(String registroAns);

    List<DespesaConsolidada> findByRegistroAns(String registroAns);

    @Query("SELECT SUM(d.valor) FROM DespesaConsolidada d")
    BigDecimal totalDespesas();

    @Query("SELECT AVG(d.valor) FROM DespesaConsolidada d")
    BigDecimal mediaDespesas();

    @Query("SELECT d.razaoSocial FROM DespesaConsolidada d GROUP BY d.razaoSocial ORDER BY SUM(d.valor) DESC")
    Page<String> top5Operadoras(Pageable pageable);
}