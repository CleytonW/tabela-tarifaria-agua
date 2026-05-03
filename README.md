# Water Tariff API

API REST para gerenciamento e cálculo de tarifas de água com base em categorias de consumidores e faixas de consumo progressivas.

## Tecnologias

- Java 21
- Spring Boot 4.x.x
- PostgreSQL
- Maven

## Pré-requisitos

- Java 21
- PostgreSQL rodando localmente
- Maven 3.8+

## Configuração do Banco de Dados

Crie o banco de dados via pgAdmin ou DBeaver:

```sql
CREATE DATABASE water_tariff_db;
```

Configure as credenciais em `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/water_tariff_db
spring.datasource.username=postgres
spring.datasource.password=suasenha

# Configuração equivalente a scripts SQL (JPA Schema Generation)
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

## Como Executar

```bash
mvn spring-boot:run
```

A aplicação sobe em `http://localhost:8080`.

## Como Testar a Aplicação

**Testes Manuais:**
Você pode testar a API manualmente enviando requisições (via cURL, Postman ou Insomnia) para `http://localhost:8080` utilizando os exemplos de payload listados abaixo.

## Endpoints

### Tabelas Tarifárias

#### Criar tabela tarifária
`POST /api/tabela-tarifarias`

**Exemplo de Body:**
```json
{
  "nome": "Tarifa Água 2024",
  "dataVigencia": "2024-05-01",
  "categorias": [
    {
      "tipo": "INDUSTRIAL",
      "faixas": [
        {
          "inicioM3": 0,
          "fimM3": 10,
          "valorUnitario": 1.00
        },
        {
          "inicioM3": 11,
          "fimM3": 20,
          "valorUnitario": 2.00
        },
        {
          "inicioM3": 21,
          "fimM3": 99999,
          "valorUnitario": 3.00
        }
      ]
    },
    {
      "tipo": "PARTICULAR",
      "faixas": [
        {
          "inicioM3": 0,
          "fimM3": 15,
          "valorUnitario": 1.50
        },
        {
          "inicioM3": 16,
          "fimM3": 99999,
          "valorUnitario": 4.00
        }
      ]
    }
  ]
}
```

**Exemplo de Resposta (201 Created):**
```json
{
    "categorias": [
        {
            "faixas": [
                {
                    "fimM3": 10,
                    "id": 16,
                    "inicioM3": 0,
                    "valorUnitario": 1.00
                },
                {
                    "fimM3": 20,
                    "id": 17,
                    "inicioM3": 11,
                    "valorUnitario": 2.00
                },
                {
                    "fimM3": 99999,
                    "id": 18,
                    "inicioM3": 21,
                    "valorUnitario": 3.00
                }
            ],
            "id": 7,
            "tipo": "INDUSTRIAL"
        },
        {
            "faixas": [
                {
                    "fimM3": 15,
                    "id": 19,
                    "inicioM3": 0,
                    "valorUnitario": 1.50
                },
                {
                    "fimM3": 99999,
                    "id": 20,
                    "inicioM3": 16,
                    "valorUnitario": 4.00
                }
            ],
            "id": 8,
            "tipo": "PARTICULAR"
        }
    ],
    "dataVigencia": "2024-05-01",
    "id": 4,
    "nome": "Tarifa Água 2024"
}
```

#### Listar tabelas tarifárias
`GET /api/tabelas-tarifarias`

**Exemplo de Resposta (200 OK):**
```json
[
    {
        "categorias": [
            {
                "faixas": [
                    {
                        "fimM3": 10,
                        "id": 16,
                        "inicioM3": 0,
                        "valorUnitario": 1.00
                    },
                    {
                        "fimM3": 20,
                        "id": 17,
                        "inicioM3": 11,
                        "valorUnitario": 2.00
                    },
                    {
                        "fimM3": 99999,
                        "id": 18,
                        "inicioM3": 21,
                        "valorUnitario": 3.00
                    }
                ],
                "id": 7,
                "tipo": "INDUSTRIAL"
            },
            {
                "faixas": [
                    {
                        "fimM3": 15,
                        "id": 19,
                        "inicioM3": 0,
                        "valorUnitario": 1.50
                    },
                    {
                        "fimM3": 99999,
                        "id": 20,
                        "inicioM3": 16,
                        "valorUnitario": 4.00
                    }
                ],
                "id": 8,
                "tipo": "PARTICULAR"
            }
        ],
        "dataVigencia": "2024-05-01",
        "id": 4,
        "nome": "Tarifa Água 2024"
    }
]
```

#### Deletar tabela tarifária
`DELETE /api/tabelas-tarifarias/{id}`

**Exemplo de Request:**
`DELETE /api/tabelas-tarifarias/1`

**Exemplo de Resposta (204 No Content):**
*(Sem corpo na resposta)*

---

### Cálculo

#### Calcular valor a pagar
`POST /api/calculos`

**Exemplo de Body:**
```json
{
  "categoria": "INDUSTRIAL",
  "consumo": 18
}
```

**Resposta:**
```json
{
  "categoria": "INDUSTRIAL",
  "consumoTotal": 18,
  "valorTotal": 26.00,
  "detalhamento": [
    {
      "faixa": {
        "inicio": 0,
        "fim": 10
      },
      "m3Cobrados": 10,
      "valorUnitario": 1.00,
      "subtotal": 10.00
    },
    {
      "faixa": {
        "inicio": 11,
        "fim": 20
      },
      "m3Cobrados": 8,
      "valorUnitario": 2.00,
      "subtotal": 16.00
    }
  ]
}
```

## Regras de Negócio

- As faixas são cobradas de forma **progressiva** — cada m³ é cobrado na faixa correspondente.
- As faixas devem iniciar em **0 m³**.
- Não pode haver **sobreposição** entre faixas da mesma categoria.
- O início de cada faixa deve ser **menor que o fim**.
- As faixas devem cobrir **qualquer consumo informado** (sempre ter uma faixa com teto muito alto).

## Exemplos de Validações e Erros Tratados

Nossa API possui uma camada global de tratamento de exceções (`GlobalExceptionHandler`) com respostas limpas. Seguem exemplos para forçar as validações da regra de negócio:

### 1. Faixa não começando em zero
O início da primeira faixa de qualquer categoria deve obrigatóriamente iniciar em 0.

**Requisição:** `POST /api/tabelas-tarifarias`
```json
{
  "nome": "Tabela Errada 1",
  "dataVigencia": "2025-01-01",
  "categorias": [
    {
      "tipo": "INDUSTRIAL",
      "faixas": [
        { "inicioM3": 2, "fimM3": 10, "valorUnitario": 1.00 },
        { "inicioM3": 11, "fimM3": 99999, "valorUnitario": 2.00 }
      ]
    }
  ]
}
```
**Resposta (422 Unprocessable Entity):**
```json
{
  "mensagem": "A primeira faixa de consumo da categoria INDUSTRIAL deve iniciar em 0 m³.",
  "status": 422
}
```

### 2. Faixas com Sobreposição ou Buracos
As faixas precisam ser subsequentes perfeitamente, sem sobrepostas e sem lacunas entre os M³. No exemplo simulou-se um pulo do 10 para o 15.

**Requisição:** `POST /api/tabelas-tarifarias`
```json
{
  "nome": "Tabela Errada 2",
  "dataVigencia": "2025-01-01",
  "categorias": [
    {
      "tipo": "INDUSTRIAL",
      "faixas": [
        { "inicioM3": 0, "fimM3": 10, "valorUnitario": 1.00 },
        { "inicioM3": 15, "fimM3": 99999, "valorUnitario": 2.00 } 
      ]
    }
  ]
}
```
**Resposta (422 Unprocessable Entity):**
```json
{
  "mensagem": "As faixas da categoria INDUSTRIAL possuem sobreposição ou buracos. A faixa que inicia em 15 deveria iniciar em 11.",
  "status": 422
}
```

### 3. Cálculo de Consumo sobre Categoria Inexistente
Caso a requisição de cálculo informe uma categoria não mapeada ou que não tenha sido configurada na última tabela ativa no banco.

**Requisição:** `POST /api/calculos`
```json
{
  "categoria": "COMERCIAL",
  "consumo": 18
}
```
**Resposta (404 Not Found):**
```json
{
  "mensagem": "Nenhuma tabela tarifária ativa encontrada para a categoria: COMERCIAL",
  "status": 404
}
```

## Estrutura do Projeto

```
src/main/java/com/cleytonmelo/watertariff/
├── controller/     # Controladores REST que recebem as chamadas HTTP
├── dto/            # Records (Data Transfer Objects) para Request e Response
├── exception/      # Tratamento global de erros e classes de Exceção
├── model/          # Classes de Entidade JPA mapeadas para o banco
│   └── enums/
├── repository/     # Interfaces do Spring Data JPA para acesso a dados
└── service/        # Camada de lógica de negócios e cálculos
``` 
