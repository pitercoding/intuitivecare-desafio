package com.pitercoding.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Despesa {
    private String cnpj;
    private String razaoSocial;
    private int ano;
    private int trimestre;
    private BigDecimal valor;
    private String uf;
    private String nomeFantasia;
    private String modalidade;

    // construtor -> campos essenciais
    public Despesa(String cnpj, String razaoSocial, int ano, int trimestre, BigDecimal valor) {
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.ano = ano;
        this.trimestre = trimestre;
        this.valor = valor;
    }

    public String[] toArray() {
        return new String[]{
                cnpj,
                razaoSocial,
                String.valueOf(ano),
                String.valueOf(trimestre),
                valor.toString(),
                uf != null ? uf : "",
                nomeFantasia != null ? nomeFantasia : "",
                modalidade != null ? modalidade : ""
        };
    }

}