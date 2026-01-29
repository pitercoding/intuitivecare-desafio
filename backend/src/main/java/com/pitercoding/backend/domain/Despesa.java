package com.pitercoding.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Despesa {

    private String cnpj;
    private String razaoSocial;
    private int ano;
    private int trimestre;
    private BigDecimal valor;

    private String uf;
    private String nomeFantasia;
    private String modalidade;

    public String[] toArray() {
        return new String[]{
                cnpj,
                razaoSocial,
                String.valueOf(ano),
                String.valueOf(trimestre),
                valor.toString(),
                uf,
                nomeFantasia,
                modalidade
        };
    }
}
