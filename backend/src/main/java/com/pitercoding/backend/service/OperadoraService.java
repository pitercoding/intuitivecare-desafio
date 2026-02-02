package com.pitercoding.backend.service;

import com.pitercoding.backend.domain.DespesaConsolidada;
import com.pitercoding.backend.domain.Operadora;
import com.pitercoding.backend.repository.DespesaConsolidadaRepository;
import com.pitercoding.backend.repository.OperadoraRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OperadoraService {

    private final OperadoraRepository operadoraRepository;
    private final DespesaConsolidadaRepository despesaConsolidadaRepository;

    public OperadoraService(OperadoraRepository repository, DespesaConsolidadaRepository despesaConsolidadaRepository) {
        this.operadoraRepository = repository;
        this.despesaConsolidadaRepository = despesaConsolidadaRepository;
    }

    public Page<Operadora> listar(Pageable pageable) {
        return operadoraRepository.findAll(pageable);
    }

    public Optional<Operadora> findByRegistroAns(String registroAns) {
        return operadoraRepository.findById(registroAns);
    }

    public List<DespesaConsolidada> findDespesasByRegistroAns(String registroAns) {
        return despesaConsolidadaRepository.findByRegistroAnsOrderByAnoAscTrimestreAsc(registroAns);
    }

    public Map<String, Object> calcularEstatisticas() {
        BigDecimal total = despesaConsolidadaRepository.totalDespesas();
        BigDecimal media = despesaConsolidadaRepository.mediaDespesas();
        List<String> top5 = despesaConsolidadaRepository.top5Operadoras(PageRequest.of(0, 5)).getContent();

        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("media", media);
        result.put("top5", top5);
        return result;
    }
}