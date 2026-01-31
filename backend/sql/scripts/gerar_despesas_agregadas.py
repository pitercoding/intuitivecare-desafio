import mysql.connector

# =========================
# CONEXÃO COM O BANCO
# =========================
conn = mysql.connector.connect(
    host="localhost",
    user="root",
    password="",
    database="intuitivecare_db"
)

# cursor NORMAL (não iterar nele enquanto insere)
cursor_select = conn.cursor(dictionary=True)
cursor_insert = conn.cursor()

# =========================
# LIMPAR TABELA DESTINO
# =========================
print("Limpando tabela despesas_agregadas...")
cursor_insert.execute("TRUNCATE TABLE despesas_agregadas")
conn.commit()

# =========================
# BUSCAR AGREGAÇÕES
# =========================
print("Buscando agregações da tabela despesas_consolidadas...")

select_sql = """
SELECT
    cnpj,
    razao_social,
    uf,
    SUM(valor) AS total,
    AVG(valor) AS media,
    STDDEV_POP(valor) AS desvio_padrao
FROM despesas_consolidadas
GROUP BY cnpj, razao_social, uf
"""

cursor_select.execute(select_sql)

# Lê TODOS os resultados antes de começar a inserir
rows = cursor_select.fetchall()

print(f"Total de registros agregados encontrados: {len(rows)}")

# =========================
# INSERÇÃO EM LOTES
# =========================
insert_sql = """
INSERT INTO despesas_agregadas
(cnpj, razao_social, uf, total, media, desvio_padrao)
VALUES (%s, %s, %s, %s, %s, %s)
"""

batch = []
batch_size = 2000
total_inserted = 0

for row in rows:
    batch.append((
        row["cnpj"],
        row["razao_social"],
        row["uf"],
        round(row["total"], 2) if row["total"] is not None else 0,
        round(row["media"], 2) if row["media"] is not None else 0,
        round(row["desvio_padrao"], 2) if row["desvio_padrao"] is not None else 0
    ))

    if len(batch) >= batch_size:
        cursor_insert.executemany(insert_sql, batch)
        conn.commit()
        total_inserted += len(batch)
        print(f"Inseridos {total_inserted} registros...")
        batch.clear()

# Último lote
if batch:
    cursor_insert.executemany(insert_sql, batch)
    conn.commit()
    total_inserted += len(batch)

print(f"Processo finalizado com sucesso! Total inserido: {total_inserted}")

# =========================
# FECHAR CONEXÕES
# =========================
cursor_select.close()
cursor_insert.close()
conn.close()
