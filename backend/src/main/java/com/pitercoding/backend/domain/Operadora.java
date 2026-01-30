package com.pitercoding.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Objetivo: Representar dados cadastrais da ANS
 */

@Entity
@Table(name = "operadora")
@Data
public class Operadora {

    @Id
    @Column(length = 14)
    private String cnpj;

    @Column(name = "razao_social", nullable = false)
    private String razaoSocial;

    @Column(name = "nome_fantasia")
    private String nomeFantasia;

    @Column(name = "registro_ans")
    private String registroAns;

    @Column(name = "uf", length = 2)
    private String uf;

    @Column(name = "modalidade")
    private String modalidade;
}
