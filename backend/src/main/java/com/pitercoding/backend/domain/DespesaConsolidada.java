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

    @Column(name = "registro_ans", length = 14, nullable = false)
    private String registroAns;

    @Column(name = "cnpj", length = 14, nullable = false)
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

    public DespesaConsolidada(String cnpj, String razaoSocial, int ano, int trimestre, BigDecimal valor, String registroAns) {
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.ano = ano;
        this.trimestre = trimestre;
        this.valor = valor;
        this.registroAns = registroAns;
    }

    public String[] toArray() {
        return new String[]{
                registroAns,
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