package com.pitercoding.backend.service;

import com.pitercoding.backend.domain.DespesaConsolidada;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Responsabilidade: Validar uma Despesa inteira, usando o CnpjValidator.
 * Centraliza regras de negócio: CNPJ válido, Valor > 0 e Razão social não vazia
 */

@Service
public class ValidationService {

    private static final Logger log = LoggerFactory.getLogger(ValidationService.class);
    private final CnpjValidator cnpjValidator;

    public ValidationService(CnpjValidator cnpjValidator) {
        this.cnpjValidator = cnpjValidator;
    }

    public boolean isDespesaValida(DespesaConsolidada despesa) {
        if (!cnpjValidator.isValido(despesa.getCnpj())) {
            log.warn("CNPJ inválido: {}", despesa.getCnpj());
            return false;
        }

        if (despesa.getValor() == null || despesa.getValor().signum() <= 0) {
            log.warn("Valor inválido para CNPJ {}", despesa.getCnpj());
            return false;
        }

        if (despesa.getRazaoSocial() == null || despesa.getRazaoSocial().isBlank()) {
            log.warn("Razão social vazia para CNPJ {}", despesa.getCnpj());
            return false;
        }

        return true;
    }
}
