package com.pitercoding.backend.domain;

import lombok.Data;

/**
 * Objetivo: Representar dados cadastrais da ANS
 */

@Data
public class Operadora {
    private String cnpj;
    private String razaoSocial;
    private String nomeFantasia;
    private String uf;
    private String modalidade;
}
