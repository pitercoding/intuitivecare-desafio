package com.pitercoding.backend.domain;

import lombok.Data;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "despesas_agregadas")
@Data
public class DespesaAgregada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cnpj;
    private String razaoSocial;
    private String uf;

    @Column(precision = 15, scale = 2)
    private BigDecimal total;

    @Column(precision = 15, scale = 2)
    private BigDecimal media;

    @Column(precision = 15, scale = 2)
    private BigDecimal desvioPadrao;
}

