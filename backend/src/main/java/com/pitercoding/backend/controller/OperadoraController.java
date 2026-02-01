package com.pitercoding.backend.controller;

import com.pitercoding.backend.domain.DespesaConsolidada;
import com.pitercoding.backend.domain.Operadora;
import com.pitercoding.backend.service.EstatisticasService;
import com.pitercoding.backend.service.OperadoraService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/operadoras")
public class OperadoraController {

    private final OperadoraService operadoraService;
    private final EstatisticasService estatisticasService;

    public OperadoraController(OperadoraService service, EstatisticasService estatisticasService) {
        this.operadoraService = service;
        this.estatisticasService = estatisticasService;
    }

    /**
     * GET /api/operadoras
     * Retorna uma lista paginada de todas as operadoras.
     * Parâmetros opcionais: page (página atual) e limit (quantidade por página).
     * Retorna dados + metadados (total, página, limite).
     */
    @GetMapping
    public Page<Operadora> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return operadoraService.listar(PageRequest.of(page, size));
    }

    /**
     * GET /api/operadoras/{cnpj}
     * Retorna os detalhes de uma operadora específica pelo CNPJ.
     * Se não encontrar, retorna 404 Not Found.
     */
    @GetMapping("/{cnpj}")
    public ResponseEntity<Operadora> getOperadora(@PathVariable String cnpj) {
        return operadoraService.findByCnpj(cnpj)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/operadoras/{cnpj}/despesas
     * Retorna todas as despesas consolidadas de uma operadora pelo CNPJ.
     * Se não houver despesas, retorna 404 Not Found.
     */
    @GetMapping("/{cnpj}/despesas")
    public ResponseEntity<List<DespesaConsolidada>> getDespesasPorOperadora(@PathVariable String cnpj) {
        List<DespesaConsolidada> despesas = operadoraService.findDespesasByCnpj(cnpj);
        if (despesas.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(despesas);
    }

    /**
     * GET /api/operadoras/estatisticas
     * Retorna estatísticas agregadas das operadoras:
     * total de despesas, média de despesas e top 5 operadoras por valor total.
     */
    @GetMapping("/estatisticas")
    public ResponseEntity<Map<String, Object>> getEstatisticas() {
        Map<String, Object> stats = estatisticasService.calcularEstatisticas();
        return ResponseEntity.ok(stats);
    }
}
