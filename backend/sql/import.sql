-- Scripts de importação (LOAD DATA)
-- Registros inválidos → ignorados na importação
-- Datas inconsistentes → normalizadas para o último mês do trimestre
-- Encoding UTF-8 assumido

-- =========================
-- IMPORTAR OPERADORAS
-- =========================
LOAD DATA LOCAL INFILE 'D:/programacao/intuitivecare-desafio/backend/sql/raw/operadoras_ativas.csv'
INTO TABLE operadora
CHARACTER SET utf8
FIELDS TERMINATED BY ';'
ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 ROWS
(@dummy, @cnpj, @razao_social, @nome_fantasia, @modalidade, @dummy1, @dummy2, @dummy3, @dummy4, @dummy5, @uf, @dummy6, @dummy7, @dummy8, @dummy9, @dummy10, @dummy11, @dummy12, @registro_ans)
SET
    cnpj = TRIM(REPLACE(@cnpj, '"', '')),
    razao_social = TRIM(@razao_social),
    nome_fantasia = TRIM(@nome_fantasia),
    modalidade = TRIM(@modalidade),
    uf = TRIM(@uf),
    registro_ans = TRIM(@registro_ans);

-- =========================
-- IMPORTAR DADOS BRUTOS ANS
-- =========================
-- 1T2025
LOAD DATA LOCAL INFILE 'D:/programacao/intuitivecare-desafio/backend/sql/extracted/1T2025/1T2025_mysql.csv'
INTO TABLE despesas_raw_ans
CHARACTER SET utf8
FIELDS TERMINATED BY ';'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(DATA, REG_ANS, CD_CONTA_CONTABIL, DESCRICAO, VL_SALDO_INICIAL, VL_SALDO_FINAL);

-- 2T2025
LOAD DATA LOCAL INFILE 'D:/programacao/intuitivecare-desafio/backend/sql/extracted/2T2025/2T2025_mysql.csv'
INTO TABLE despesas_raw_ans
CHARACTER SET utf8
FIELDS TERMINATED BY ';'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(DATA, REG_ANS, CD_CONTA_CONTABIL, DESCRICAO, VL_SALDO_INICIAL, VL_SALDO_FINAL);

-- 3T2025
LOAD DATA LOCAL INFILE 'D:/programacao/intuitivecare-desafio/backend/sql/extracted/3T2025/3T2025_mysql.csv'
INTO TABLE despesas_raw_ans
CHARACTER SET utf8
FIELDS TERMINATED BY ';'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(DATA, REG_ANS, CD_CONTA_CONTABIL, DESCRICAO, VL_SALDO_INICIAL, VL_SALDO_FINAL);
