-- =====================================================
-- QUERY 1: Top 5 operadoras com maior crescimento percentual
-- =====================================================
WITH valores AS (
    SELECT
        cnpj,
        MIN(data_referencia) AS primeira_data,
        MAX(data_referencia) AS ultima_data
    FROM despesas_consolidadas
    GROUP BY cnpj
),
base AS (
    SELECT
        d.cnpj,
        MAX(CASE WHEN d.data_referencia = v.primeira_data THEN d.valor END) AS valor_inicial,
        MAX(CASE WHEN d.data_referencia = v.ultima_data THEN d.valor END) AS valor_final
    FROM despesas_consolidadas d
    JOIN valores v ON d.cnpj = v.cnpj
    GROUP BY d.cnpj
)
SELECT
    cnpj,
    ((valor_final - valor_inicial) / valor_inicial) * 100 AS crescimento_percentual
FROM base
WHERE valor_inicial > 0
ORDER BY crescimento_percentual DESC
LIMIT 5;

-- =====================================================
-- QUERY 2: Top 5 UFs com maior volume de despesas
-- =====================================================
SELECT
    o.uf,
    SUM(d.valor) AS total_despesas,
    AVG(d.valor) AS media_por_operadora
FROM despesas_consolidadas d
JOIN operadora o ON o.cnpj = d.cnpj
GROUP BY o.uf
ORDER BY total_despesas DESC
LIMIT 5;

-- =====================================================
-- QUERY 3: Operadoras acima da média em pelo menos 2 trimestres
-- =====================================================
WITH media_geral AS (
    SELECT AVG(valor) AS media FROM despesas_consolidadas
),
acima_media AS (
    SELECT
        cnpj,
        COUNT(*) AS qtd_trimestres
    FROM despesas_consolidadas, media_geral
    WHERE valor > media
    GROUP BY cnpj
)
SELECT COUNT(*) AS operadoras_acima_media
FROM acima_media
WHERE qtd_trimestres >= 2;