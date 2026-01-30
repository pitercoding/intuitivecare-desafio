
-- DDL – Criação das tabelas

-- =========================
-- TABELA: operadora
-- =========================
CREATE TABLE operadora (
    cnpj VARCHAR(14) PRIMARY KEY,
    razao_social VARCHAR(255) NOT NULL,
    nome_fantasia VARCHAR(255),
    modalidade VARCHAR(100),
    uf CHAR(2),
    registro_ans VARCHAR(20)
);

-- =========================
-- TABELA: despesas_consolidadas
-- =========================
CREATE TABLE despesas_consolidadas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cnpj VARCHAR(14) NOT NULL,
    ano INT NOT NULL,
    trimestre INT NOT NULL,
    data_referencia DATE NOT NULL,
    valor DECIMAL(15,2) NOT NULL,

    CONSTRAINT fk_despesa_operadora
        FOREIGN KEY (cnpj) REFERENCES operadora(cnpj)
);

CREATE INDEX idx_despesas_periodo
    ON despesas_consolidadas (ano, trimestre);

-- =========================
-- TABELA: despesas_agregadas
-- =========================
CREATE TABLE despesas_agregadas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cnpj VARCHAR(14) NOT NULL,
    uf CHAR(2),
    total DECIMAL(15,2) NOT NULL,
    media DECIMAL(15,2) NOT NULL,
    desvio_padrao DECIMAL(15,2) NOT NULL,

    CONSTRAINT fk_agregada_operadora
        FOREIGN KEY (cnpj) REFERENCES operadora(cnpj)
);
