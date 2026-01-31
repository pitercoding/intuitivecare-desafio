-- =========================================
-- QUERIES ANALÍTICAS - ITEM 3.4
-- Desafio Técnico Intuitive Care
-- =========================================

-- -----------------------------------------
-- 3.4.1 Top 10 operadoras por despesa total
-- -----------------------------------------
SELECT
    cnpj,
    razao_social,
    uf,
    total
FROM despesas_agregadas
ORDER BY total DESC
LIMIT 10;

-- -----------------------------------------
-- 3.4.2 Top 10 operadoras por média de despesa
-- -----------------------------------------
SELECT
    cnpj,
    razao_social,
    uf,
    media
FROM despesas_agregadas
ORDER BY media DESC
LIMIT 10;

-- -----------------------------------------
-- 3.4.3 Top 10 operadoras por volatilidade
-- (maior desvio padrão)
-- -----------------------------------------
SELECT
    cnpj,
    razao_social,
    uf,
    desvio_padrao
FROM despesas_agregadas
ORDER BY desvio_padrao DESC
LIMIT 10;

-- -----------------------------------------
-- 3.4.4 Contas onde o desvio padrão
-- é maior que a média absoluta
-- -----------------------------------------
SELECT
    cnpj,
    razao_social,
    uf,
    total,
    media,
    desvio_padrao
FROM despesas_agregadas
WHERE desvio_padrao > ABS(media)
ORDER BY desvio_padrao DESC
LIMIT 10;

-- -----------------------------------------
-- 3.4.5 Total de despesas agregadas por UF
-- -----------------------------------------
SELECT
    uf,
    SUM(total) AS total_uf
FROM despesas_agregadas
GROUP BY uf
ORDER BY total_uf DESC;

-- -----------------------------------------
-- 3.4.6 Evolução trimestral das despesas
-- (suporte para análise temporal / frontend)
-- -----------------------------------------
SELECT
    cnpj,
    razao_social,
    ano,
    trimestre,
    SUM(valor) AS total_trimestre
FROM despesas_consolidadas
GROUP BY cnpj, razao_social, ano, trimestre
ORDER BY cnpj, ano, trimestre;
