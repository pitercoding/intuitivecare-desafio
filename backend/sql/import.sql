-- Scripts de importação (LOAD DATA)
-- Registros inválidos → ignorados na importação
-- Datas inconsistentes → normalizadas para o último mês do trimestre
-- Encoding UTF-8 assumido

-- =========================
-- IMPORTAR OPERADORAS
-- =========================
LOAD DATA LOCAL INFILE '/caminho/operadoras_ativas.csv'
INTO TABLE operadora
FIELDS TERMINATED BY ';'
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(cnpj, razao_social, nome_fantasia, modalidade, uf, registro_ans);

-- =========================
-- IMPORTAR DESPESAS CONSOLIDADAS
-- =========================
LOAD DATA LOCAL INFILE '/caminho/consolidado_despesas.csv'
INTO TABLE despesas_consolidadas
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(cnpj, razao_social, ano, trimestre, valor, uf, nome_fantasia, modalidade)
SET
    data_referencia = STR_TO_DATE(
        CONCAT(ano, '-', trimestre * 3, '-01'),
        '%Y-%m-%d'
    );

-- =========================
-- IMPORTAR DESPESAS AGREGADAS
-- =========================
LOAD DATA LOCAL INFILE '/caminho/despesas_agregadas.csv'
INTO TABLE despesas_agregadas
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(cnpj, razao_social, uf, total, media, desvio_padrao);
