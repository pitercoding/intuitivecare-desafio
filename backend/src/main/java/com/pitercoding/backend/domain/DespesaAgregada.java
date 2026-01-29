package com.pitercoding.backend.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DespesaAgregada {
    private String cnpj;
    private String razaoSocial;
    private String uf;
    private BigDecimal total;
    private BigDecimal media;
    private BigDecimal desvioPadrao;
}
