package com.pitercoding.backend.service;

import com.pitercoding.backend.domain.DespesaConsolidada;
import com.pitercoding.backend.domain.Operadora;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Objetivo:
 * 1. Receber a lista de despesas e o mapa de operadoras.
 * 2. Preencher UF, nomeFantasia e modalidade.
 * 3. Registrar despesas que não encontrarem match para exportação futura.
 */

@Service
public class DespesaEnrichmentService {

    private static final Logger log = LoggerFactory.getLogger(DespesaEnrichmentService.class);

    public List<DespesaConsolidada> enrichDespesas(List<DespesaConsolidada> despesas,
                                                   Map<String, Operadora> mapaOperadoras,
                                                   List<DespesaConsolidada> semMatch) {

        List<DespesaConsolidada> enriquecidas = new ArrayList<>();

        for (DespesaConsolidada d : despesas) {
            Operadora op = mapaOperadoras.get(d.getCnpj());
            if (op != null) {
                d.setUf(op.getUf());
                d.setNomeFantasia(op.getNomeFantasia());
                d.setModalidade(op.getModalidade());
                enriquecidas.add(d);
            } else {
                semMatch.add(d);
                log.warn("CNPJ sem match no cadastro: {}", d.getCnpj());
            }
        }

        return enriquecidas;
    }
}
