package com.pitercoding.backend.service;

import com.pitercoding.backend.domain.Despesa;
import com.pitercoding.backend.domain.DespesaAgregada;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Objetivo: Gerar estatísticas.
 * Entrada: Milhares de registros de despesas "soltos".
 * Processamento: Agrupa por CNPJ -> Soma -> Calcula Média -> Calcula Variância.
 * Saída: Uma lista limpa onde cada item representa uma empresa e seu comportamento financeiro.
 */

@Service
public class AggregationService {

    public List<DespesaAgregada> aggregate(List<Despesa> despesas) {
        Map<String, List<Despesa>> agrupado =
                despesas.stream().collect(Collectors.groupingBy(Despesa::getCnpj));

        List<DespesaAgregada> resultado = new ArrayList<>();

        for (var entry : agrupado.entrySet()) {
            List<BigDecimal> valores = entry.getValue()
                    .stream().map(Despesa::getValor).toList();

            BigDecimal total = valores.stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal media =
                    total.divide(BigDecimal.valueOf(valores.size()), BigDecimal.ROUND_HALF_UP);

            double desvio = Math.sqrt(
                    valores.stream()
                            .mapToDouble(v -> Math.pow(v.subtract(media).doubleValue(), 2))
                            .average().orElse(0)
            );

            DespesaAgregada despesaAgregada = new DespesaAgregada();
            despesaAgregada.setCnpj(entry.getKey());
            despesaAgregada.setRazaoSocial(entry.getValue().get(0).getRazaoSocial());
            despesaAgregada.setUf(entry.getValue().get(0).getUf());
            despesaAgregada.setTotal(total);
            despesaAgregada.setMedia(media);
            despesaAgregada.setDesvioPadrao(BigDecimal.valueOf(desvio));

            resultado.add(despesaAgregada);
        }
        return resultado;
    }
}