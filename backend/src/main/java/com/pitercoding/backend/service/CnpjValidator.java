package com.pitercoding.backend.service;

import org.springframework.stereotype.Service;

/**
 * Classe utilitária
 * Responsabilidade:
 * 1. Limpar máscara
 * 2. Validar tamanho
 * 3. Validar dígitos verificadores
 */

@Service
public class CnpjValidator {

    public boolean isValido(String cnpj) {
        if (cnpj == null) return false;

        cnpj = cnpj.replaceAll("\\D", "");

        if (cnpj.length() != 14) return false;

        try {
            int soma = 0;
            int peso = 2;
            for (int i = 11; i >= 0; i--) {
                soma += Character.getNumericValue(cnpj.charAt(i)) * peso;
                peso = peso == 9 ? 2 : peso + 1;
            }
            int dig1 = soma % 11 < 2 ? 0 : 11 - (soma % 11);

            soma = 0;
            peso = 2;
            for (int i = 12; i >= 0; i--) {
                soma += Character.getNumericValue(cnpj.charAt(i)) * peso;
                peso = peso == 9 ? 2 : peso + 1;
            }
            int dig2 = soma % 11 < 2 ? 0 : 11 - (soma % 11);

            return dig1 == Character.getNumericValue(cnpj.charAt(12))
                    && dig2 == Character.getNumericValue(cnpj.charAt(13));
        } catch (Exception e) {
            return false;
        }
    }


}
