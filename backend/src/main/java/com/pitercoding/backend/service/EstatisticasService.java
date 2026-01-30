package com.pitercoding.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Service
public class EstatisticasService {

    private final OperadoraService operadoraService;

    @Autowired
    public EstatisticasService(OperadoraService operadoraService) {
        this.operadoraService = operadoraService;
    }

    public Map<String, Object> calcularEstatisticas() {
        // Delegando para OperadoraService
        return operadoraService.calcularEstatisticas();
    }
}