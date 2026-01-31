# Desafio Técnico – Intuitive Care

## Objetivo

Este projeto importa, normaliza, consolida e analisa dados financeiros públicos da ANS (Agência Nacional de Saúde Suplementar), permitindo consultas analíticas sobre despesas das operadoras de planos de saúde.

Ele contempla:

- Estruturação de banco de dados relacional (MySQL)
- Processamento e agregação de CSVs com Python
- Consultas analíticas SQL
- Integração futura com API REST e frontend

---

## 🧱 Tecnologias Utilizadas

- **MySQL 8.0+**
- **Java 17+ / Spring Boot** (backend principal)
- **Python 3.8+** (para agregação de dados)
- **Angular** (frontend)
- **CSV / ZIP** (dados públicos ANS)
- **MySQL Workbench**

---

## 📁 Estrutura do Projeto

```bash
intuitivecare-desafio/
├── backend/
│   ├── sql/
│   │   ├── extracted/      # CSVs processados por trimestre (1T2025, 2T2025, 3T2025)
│   │   ├── raw/            # ZIPs originais e CSV de operadoras (operadoras_ativas.csv)
│   │   ├── scripts/
│   │   │   └── gerar_despesas_agregadas.py
│   │   ├── ddl.sql         # Criação das tabelas
│   │   ├── import.sql      # Importação dos CSVs para MySQL
│   │   ├── queries_3_4.sql # Queries analíticas (item 3.4)
│   ├── src/                # Código-fonte backend Java
│   └── pom.xml             # Dependências Maven
├── .gitignore              
├── LICENSE                 
└── README.md               # Documentação principal
```

---

## Processamento de Dados

1. **Importação de dados da ANS**
   - ZIPs dos últimos 3 trimestres
   - Extração automática
   - Filtragem por Despesas com Eventos/Sinistros

2. **Consolidação**
   - Agrupamento de dados por CNPJ, Razão Social, Trimestre, Ano
   - Tratamento de CNPJs duplicados, valores zerados ou negativos
   - CSV consolidado: `consolidado_despesas.csv`

3. **Enriquecimento**
   - Join com cadastro de operadoras ativas (`operadoras_ativas.csv`)
   - Adição de `registro_ans`, `modalidade`, `uf`
   - Tratamento de registros sem match ou duplicados

4. **Agregação**
   - Script Python: `gerar_despesas_agregadas.py`
   - Calcula: total, média, desvio padrão por operadora/UF
   - Limpa tabela `despesas_agregadas` antes de inserir

---

## Banco de Dados (MySQL)

Foram criadas **tabelas normalizadas**, separando responsabilidades:

- operadora: dados cadastrais das operadoras ativas
- despesas_consolidadas: despesas por operadora, trimestre e ano
- despesas_agregadas: métricas calculadas (total, média e desvio padrão) via script Python

### Trade-off técnico – Normalização

**Opção escolhida**: Tabelas normalizadas

Justificativa:

- Evita duplicação de dados cadastrais
- Facilita manutenção e atualização
- Queries analíticas continuam simples com JOINs
- Escala melhor para crescimento futuro do volume de dados

### Tipos de Dados

- **DECIMAL(15,2)**: valores monetários, evita problemas de precisão
- **DATE**: datas de referência, melhor indexação e comparação

### Importação dos Dados

Feita via `LOAD DATA INFILE`, com cuidados:

- Encoding UTF-8
- Ignorar headers
- Conversão automática de valores
- Normalização de CNPJ (apenas números)

Tratamento de inconsistências:

| Problema encontrado             | Estratégia adotada              |
| ------------------------------- | ------------------------------- |
| Valores NULL em campos críticos | Registro rejeitado              |
| Strings em campos numéricos     | Conversão implícita ou rejeição |
| Datas inconsistentes            | Padronização no processamento   |

## Agregação de Despesas (Python)

- Script: `gerar_despesas_agregadas.py`
- Funções principais:
  - Agrupar despesas por operadora e UF
  - Calcular total, média e desvio padrão
  - Popular a tabela `despesas_agregadas` no MySQL
- Estratégia: **batch insert** de 2000 registros por vez para performance

## Queries Analíticas (SQL)

Arquivo central: `queries_3_4.sql`

- **Query 1**: Top 10 operadoras por despesa total
- **Query 2**: Top 10 operadoras por média de despesa
- **Query 3**: Top 10 operadoras por volatilidade (maior desvio padrão)
- **Query 4**: Contas onde o desvio padrão é maior que a média absoluta
- **Query 5**: Total de despesas agregadas por UF
- **Query 6**: Evolução trimestral das despesas(suporte para análise temporal / frontend)

**Decisões técnicas**: subqueries para legibilidade, cálculo de métricas complexas em Python para performance, índices em colunas críticas.

---

## Ordem recomendada de execução

```bash
# 1. Criar banco e tabelas
mysql -u root -p
CREATE DATABASE intuitivecare_db;
USE intuitivecare_db;
source backend/sql/ddl.sql;

# 2. Importar dados brutos
source backend/sql/import.sql;

# 3. Executar agregação Python
python backend/sql/scripts/gerar_despesas_agregadas.py

# 4. Executar queries analíticas
source backend/sql/queries_3_4.sql
```

---

## Integração Futura

A modelagem permite integração com uma **API REST** e **frontend** (Angular, Vue ou React), considerando:

- Paginação eficiente
- Queries indexadas por CNPJ, UF e período
- Uso direto em gráficos e dashboards

---

## Trade-offs

- **Python**: cálculo de métricas complexas e batch inserts
- **SQL**: consultas analíticas simples e seguras
- **DECIMAL**: precisão em valores financeiros
- **Normalização**: evita inconsistências e duplicação de dados
- **Frontend Angular**: experiência prévia, fácil integração com API REST

---

## Observações

- Valores negativos representam glosas ou reversões contábeis
- UF e outras informações podem ser enriquecidas futuramente
- Todos os trade-offs documentados no README

---

## Autor

**Piter Gomes** — Aluno de Ciências da Computação (6º Semestre) & Desenvolvedor Full-Stack

📧 [Email](mailto:piterg.bio@gmail.com) | 💼 [LinkedIn](https://www.linkedin.com/in/piter-gomes-4a39281a1/) | 💻 [GitHub](https://github.com/pitercoding) | 🌐 [Portfolio](https://portfolio-pitergomes.vercel.app/)
