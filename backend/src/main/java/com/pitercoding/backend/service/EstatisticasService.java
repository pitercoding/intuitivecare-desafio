package com.pitercoding.backend.service;

import com.pitercoding.backend.dto.TotalPorUFDTO;
import com.pitercoding.backend.repository.DespesaConsolidadaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EstatisticasService {

    private final DespesaConsolidadaRepository repository;

    public EstatisticasService(DespesaConsolidadaRepository repository) {
        this.repository = repository;
    }

    public Map<String, Object> calcularEstatisticas() {

        BigDecimal total = repository.totalDespesas();
        BigDecimal media = repository.mediaDespesas();
        List<String> top5 =
                repository.top5Operadoras(
                        org.springframework.data.domain.PageRequest.of(0, 5)
                ).getContent();

        Map<String, BigDecimal> totalPorUF =
                repository.totalPorUF()
                        .stream()
                        .collect(Collectors.toMap(
                                TotalPorUFDTO::uf,
                                TotalPorUFDTO::total
                        ));

        Map<String, Object> response = new HashMap<>();
        response.put("total", total);
        response.put("media", media);
        response.put("top5", top5);
        response.put("totalPorUF", totalPorUF);

        return response;
    }
}