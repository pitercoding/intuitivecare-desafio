package com.pitercoding.backend.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "despesas_consolidadas")
@Data
@NoArgsConstructor
public class DespesaConsolidada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 14, nullable = false)
    private String cnpj;

    @Column(name = "razao_social", nullable = false)
    private String razaoSocial;

    private int ano;
    private int trimestre;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal valor;

    private String uf;
    private String nomeFantasia;
    private String modalidade;

    // construtor -> campos essenciais
    public DespesaConsolidada(String cnpj, String razaoSocial, int ano, int trimestre, BigDecimal valor) {
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