# Banco de Dados (MySQL)

## Objetivo

Esta etapa tem como objetivo estruturar um banco de dados relacional funcional, importar os dados processados nas etapas anteriores e executar queries analíticas para extração de insights, garantindo:

- Modelagem adequada
- Tipos de dados corretos
- Queries eficientes e legíveis
- Facilidade de integração com uma futura API e frontend

---

## Tecnologias Utilizadas

- **MySQL 8.0**
- Arquivos CSV gerados nos testes anteriores:
    - `consolidado_despesas.csv`
    - `despesas_agregadas.csv`
    - `operadoras_ativas.csv`

---

## Estrutura de Arquivos

```text
sql/
├── ddl.sql            # Criação das tabelas
├── import.sql         # Importação dos CSVs (LOAD DATA)
├── queries.sql        # Queries analíticas
└── README.md          # Documentação desta etapa
```

---

## Modelagem de Dados (DDL)

Foram criadas **tabelas normalizadas**, separando responsabilidades:

### Tabelas

- operadora
  - Dados cadastrais das operadoras ativas
- despesas_consolidadas
  - Despesas por operadora, trimestre e ano
- despesas_agregadas
  - Métricas calculadas (total, média e desvio padrão)

### Trade-off técnico – Normalização

**Opção escolhida**: Tabelas normalizadas

Justificativa:

- Evita duplicação de dados cadastrais
- Facilita manutenção e atualização
- Queries analíticas continuam simples com JOINs
- Escala melhor para crescimento futuro do volume de dados

---

## Tipos de Dados

### Valores Monetários
- **DECIMAL(15,2)**

**Justificativa:**
* Evita problemas de precisão do FLOAT
* Adequado para valores financeiros
* Compatível com agregações (SUM, AVG)

### Datas
- **DATE**

**Justificativa:**
* Representação clara de períodos
* Melhor indexação e comparação
* Evita inconsistências comuns em VARCHAR

---

## Importação dos Dados
A importação é feita via `LOAD DATA INFILE`, garantindo:
* Alta performance
* Menor consumo de memória
* Suporte nativo a grandes volumes

**Arquivo:** `import.sql`

### Cuidados adotados
* Encoding UTF-8
* Ignorar headers dos CSVs
* Conversão automática de valores
* Normalização prévia de CNPJ (somente números)

### Tratamento de inconsistências

| Problema encontrado             | Estratégia adotada              |
| ------------------------------- | ------------------------------- |
| Valores NULL em campos críticos | Registro rejeitado              |
| Strings em campos numéricos     | Conversão implícita ou rejeição |
| Datas inconsistentes            | Padronização no processamento   |

---

## Queries Analíticas
As queries analíticas estão centralizadas em um único arquivo: `queries.sql`

### Query 1 – Crescimento Percentual de Despesas
Quais as 5 operadoras com maior crescimento percentual entre o primeiro e o último trimestre analisado?

**Decisão técnica:** 
- Considera apenas operadoras com dados nos dois períodos
- Evita distorções por ausência de histórico.

### Query 2 – Top 5 UFs por Volume de Despesas
Quais os 5 estados com maiores despesas totais?

**Extensão adicional:** 
- Calcula também a média de despesas por operadora em cada UF.

### Query 3 – Operadoras Acima da Média
Quantas operadoras tiveram despesas acima da média geral em pelo menos 2 dos 3 trimestres analisados?

**Trade-off técnico:** 
- Uso de subqueries para maior legibilidade
- Evita cálculos redundantes 
- Mantém boa performance para o volume esperado

---

## Ordem de Execução Recomendada

```sql
-- 1. Criar as tabelas
source ddl.sql;

-- 2. Importar os dados
source import.sql;

-- 3. Executar as análises
source queries.sql;
```

---

## Integração Futura
A modelagem foi pensada para integração direta com uma API REST e frontend em **Angular**, permitindo:

- Paginação eficiente
- Queries indexadas por CNPJ, UF e período
- Uso direto em gráficos e dashboards

---

## Considerações Finais

Esta solução prioriza:
- Simplicidade (KISS)
- Clareza na modelagem
- Queries legíveis e documentadas
- Facilidade de evolução para ambientes produtivos

Em um cenário real, melhorias adicionais poderiam incluir:
- Views materializadas
- Particionamento por ano/trimestre
- Cache de estatísticas agregadas

---