package com.pitercoding.backend.repository;

import com.pitercoding.backend.domain.DespesaConsolidada;
import com.pitercoding.backend.dto.TotalPorUFDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DespesaConsolidadaRepository
        extends JpaRepository<DespesaConsolidada, Long> {

    // Despesas por operadora
    List<DespesaConsolidada>
    findByRegistroAnsOrderByAnoAscTrimestreAsc(String registroAns);

    List<DespesaConsolidada>
    findByRegistroAns(String registroAns);

    // Estatísticas globais
    @Query("SELECT SUM(d.valor) FROM DespesaConsolidada d")
    BigDecimal totalDespesas();

    @Query("SELECT AVG(d.valor) FROM DespesaConsolidada d")
    BigDecimal mediaDespesas();

    @Query("""
        SELECT o.razaoSocial
        FROM DespesaConsolidada d
        JOIN Operadora o ON o.registroAns = d.registroAns
        GROUP BY o.razaoSocial
        ORDER BY SUM(d.valor) DESC
    """)
    Page<String> top5Operadoras(Pageable pageable);

    // Total por UF via Operadora
    @Query("""
        SELECT new com.pitercoding.backend.dto.TotalPorUFDTO(
            o.uf,
            SUM(d.valor)
        )
        FROM DespesaConsolidada d
        JOIN Operadora o ON o.registroAns = d.registroAns
        WHERE o.uf IS NOT NULL
        GROUP BY o.uf
    """)
    List<TotalPorUFDTO> totalPorUF();
}
