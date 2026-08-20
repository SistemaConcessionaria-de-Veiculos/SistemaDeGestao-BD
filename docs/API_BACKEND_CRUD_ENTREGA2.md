# API REST - Backend CRUD

## Entrega 2 - Sistema de Gestão para Concessionária de Veículos

Esta documentação consolida os contratos REST implementados no backend da Entrega 2 para as tabelas principais do banco oficial: **CLIENTES**, **VENDEDORES**, **VEÍCULOS** e **VENDAS**.

O backend utiliza o esquema SQL oficial da disciplina e mantém:

```properties
spring.jpa.hibernate.ddl-auto=none
```

O Hibernate não cria nem altera as tabelas do banco. As entidades JPA foram mapeadas para trabalhar sobre o esquema existente.

## Base URL

Ambiente local:

```text
http://localhost:8080
```

Os endpoints descritos abaixo utilizam o prefixo `/api`.

---

# 1. Clientes

Base:

```text
/api/clientes
```

O cadastro de clientes respeita as especializações do banco:

- `FISICA` -> registro em `clientes` + `pessoas_fisicas`;
- `JURIDICA` -> registro em `clientes` + `pessoas_juridicas`.

As operações que envolvem a tabela principal e a especialização são executadas de forma transacional.

## POST `/api/clientes`

Cadastra um cliente.

### Pessoa física

```json
{
  "nome": "Mariana Albuquerque",
  "email": "mariana.albuquerque@example.com",
  "telefone": "81999998888",
  "rua": "Rua das Acacias",
  "numero": "145",
  "cep": "50000000",
  "tipo": "FISICA",
  "cpf": "52998224725",
  "cnpj": null
}
```

### Pessoa jurídica

```json
{
  "nome": "Autovale Comercio de Veiculos Ltda",
  "email": "contato@autovale.com.br",
  "telefone": "8133334455",
  "rua": "Avenida Norte",
  "numero": "2100",
  "cep": "52000000",
  "tipo": "JURIDICA",
  "cpf": null,
  "cnpj": "12345678000190"
}
```

### Respostas principais

- `201 Created` - cliente cadastrado;
- `400 Bad Request` - dados inválidos ou especialização inconsistente;
- `409 Conflict` - CPF ou CNPJ já cadastrado.

## GET `/api/clientes`

Lista todos os clientes.

## GET `/api/clientes/{idCliente}`

Busca um cliente pelo identificador.

### Respostas principais

- `200 OK` - cliente encontrado;
- `404 Not Found` - cliente inexistente.

## PUT `/api/clientes/{idCliente}`

Atualiza os dados do cliente.

O `idCliente` permanece o mesmo. A operação também pode alterar a especialização PF/PJ de forma transacional.

## DELETE `/api/clientes/{idCliente}`

Exclui o cliente e sua especialização.

### Respostas principais

- `204 No Content` - exclusão realizada;
- `404 Not Found` - cliente inexistente;
- `409 Conflict` - existem registros vinculados que impedem a exclusão.

---

# 2. Vendedores

Base:

```text
/api/vendedores
```

## POST `/api/vendedores`

Cadastra um vendedor.

### Requisição

```json
{
  "nome": "Rafael Montenegro",
  "cpf": "52998224725"
}
```

### Resposta

```json
{
  "matricula": 51,
  "nome": "Rafael Montenegro",
  "cpf": "52998224725"
}
```

### Respostas principais

- `201 Created` - vendedor cadastrado;
- `400 Bad Request` - nome ou CPF inválidos;
- `409 Conflict` - CPF já cadastrado.

## GET `/api/vendedores`

Lista os vendedores.

## GET `/api/vendedores/{matricula}`

Busca um vendedor pela matrícula.

### Respostas principais

- `200 OK` - vendedor encontrado;
- `404 Not Found` - vendedor inexistente.

## PUT `/api/vendedores/{matricula}`

Atualiza nome e CPF do vendedor.

## DELETE `/api/vendedores/{matricula}`

Exclui o vendedor.

### Respostas principais

- `204 No Content` - exclusão realizada;
- `404 Not Found` - vendedor inexistente;
- `409 Conflict` - vendedor possui registros vinculados, como vendas.

---

# 3. Veículos

Base:

```text
/api/veiculos
```

O modelo segue o DDL oficial:

- `veiculos` usa `chassi` como chave primária;
- `veiculos_novos` representa veículos novos;
- `veiculos_usados` representa veículos usados;
- placa e quilometragem pertencem somente a veículos usados;
- `veiculos.numero_nota` representa a associação do veículo com uma venda.

Status aceitos:

```text
disponivel
reservado
vendido
```

## POST `/api/veiculos`

### Veículo novo

```json
{
  "chassi": "9BDTESTE000000001",
  "numeroNota": null,
  "marca": "Fiat",
  "modelo": "Pulse Audace",
  "cor": "Cinza Silverstone",
  "dataFabricacao": "2026-03-15",
  "statusDisponibilidade": "disponivel",
  "valorVeiculo": 112490.00,
  "tipo": "NOVO",
  "placa": null,
  "quilometragem": null
}
```

### Veículo usado

```json
{
  "chassi": "9BWUSADO000000001",
  "numeroNota": null,
  "marca": "Volkswagen",
  "modelo": "T-Cross Comfortline",
  "cor": "Azul Norway",
  "dataFabricacao": "2022-08-20",
  "statusDisponibilidade": "disponivel",
  "valorVeiculo": 104900.00,
  "tipo": "USADO",
  "placa": "RZE4J82",
  "quilometragem": 38750
}
```

### Regras de especialização

Para `NOVO`:

- `placa` não deve ser informada;
- `quilometragem` não deve ser informada.

Para `USADO`:

- `placa` é obrigatória;
- `quilometragem` é obrigatória;
- a placa deve ser única.

### Respostas principais

- `201 Created` - veículo cadastrado;
- `400 Bad Request` - dados inválidos ou combinação NOVO/USADO inconsistente;
- `409 Conflict` - chassi ou placa já cadastrados.

## GET `/api/veiculos`

Lista todos os veículos.

## GET `/api/veiculos/{chassi}`

Busca um veículo pelo chassi.

### Respostas principais

- `200 OK` - veículo encontrado;
- `404 Not Found` - chassi inexistente.

## PUT `/api/veiculos/{chassi}`

Atualiza o veículo sem alterar o chassi.

A operação pode alterar a especialização entre `NOVO` e `USADO`. Nesse caso, a troca é feita de forma transacional entre `veiculos_novos` e `veiculos_usados`.

## DELETE `/api/veiculos/{chassi}`

Exclui a especialização e o registro principal do veículo.

### Respostas principais

- `204 No Content` - exclusão realizada;
- `404 Not Found` - veículo inexistente;
- `409 Conflict` - existem registros vinculados que impedem a exclusão.

---

# 4. Vendas

Base:

```text
/api/vendas
```

O modelo utiliza:

- `numeroNota` - chave primária gerada automaticamente;
- `idCliente` - cliente existente;
- `matriculaVendedor` - vendedor existente;
- `valorTotalVenda` - deve ser maior que zero;
- `dataDaVenda` - data da venda;
- `chassis` - lista opcional de veículos associados à venda.

A associação entre venda e veículo é persistida em `veiculos.numero_nota`.

## POST `/api/vendas`

Cadastra uma venda e, quando informado, vincula os veículos indicados em `chassis` na mesma transação.

### Requisição com veículo

```json
{
  "idCliente": 1,
  "matriculaVendedor": 1,
  "valorTotalVenda": 109900.00,
  "dataDaVenda": "2026-08-20",
  "chassis": [
    "9BDTRANS000000001"
  ]
}
```

### Requisição sem veículos

O campo `chassis` pode ser omitido:

```json
{
  "idCliente": 2,
  "matriculaVendedor": 2,
  "valorTotalVenda": 127450.00,
  "dataDaVenda": "2026-08-20"
}
```

### Resposta

```json
{
  "numeroNota": 52,
  "idCliente": 1,
  "matriculaVendedor": 1,
  "valorTotalVenda": 109900.00,
  "dataDaVenda": "2026-08-20",
  "chassis": [
    "9BDTRANS000000001"
  ]
}
```

### Regras de associação de veículos

- cada chassi informado deve existir;
- um veículo já vinculado a outra venda não pode ser vinculado novamente;
- o vínculo é feito por `veiculos.numero_nota`;
- a criação da venda e os vínculos com veículos fazem parte da mesma transação;
- se ocorrer erro ao vincular um veículo, a venda também é desfeita por rollback;
- associar um veículo a uma venda altera `numeroNota`, mas não altera automaticamente `statusDisponibilidade`.

### Respostas principais

- `201 Created` - venda cadastrada;
- `400 Bad Request` - valor, data ou identificadores inválidos;
- `404 Not Found` - cliente, vendedor ou veículo informado não existe;
- `409 Conflict` - algum veículo já está vinculado a outra venda.

## GET `/api/vendas`

Lista todas as vendas. Cada item inclui a lista `chassis` dos veículos atualmente associados.

## GET `/api/vendas/{numeroNota}`

Busca uma venda pelo número da nota e retorna também seus veículos associados.

### Exemplo

```json
{
  "numeroNota": 52,
  "idCliente": 1,
  "matriculaVendedor": 1,
  "valorTotalVenda": 109900.00,
  "dataDaVenda": "2026-08-20",
  "chassis": [
    "9BDTRANS000000001"
  ]
}
```

### Respostas principais

- `200 OK` - venda encontrada;
- `404 Not Found` - venda inexistente.

## PUT `/api/vendas/{numeroNota}`

Atualiza cliente, vendedor, valor e data da venda, preservando `numeroNota`.

O campo `chassis` controla os vínculos da seguinte forma:

| Conteúdo no PUT | Comportamento |
|---|---|
| campo `chassis` omitido | preserva os vínculos atuais |
| `"chassis": []` | remove todos os veículos atualmente vinculados |
| `"chassis": ["CHASSI1", "CHASSI2"]` | substitui os vínculos atuais pelos chassis informados |

### Exemplo - substituir o veículo da venda

```json
{
  "idCliente": 1,
  "matriculaVendedor": 1,
  "valorTotalVenda": 124900.00,
  "dataDaVenda": "2026-08-20",
  "chassis": [
    "9BDTRANS000000002"
  ]
}
```

A atualização dos dados da venda, o desligamento dos veículos anteriores e o vínculo dos novos veículos acontecem na mesma transação.

### Respostas principais

- `200 OK` - venda atualizada;
- `400 Bad Request` - dados inválidos;
- `404 Not Found` - venda, cliente, vendedor ou veículo inexistente;
- `409 Conflict` - tentativa de usar veículo vinculado a outra venda.

## DELETE `/api/vendas/{numeroNota}`

Exclui uma venda quando não existem registros dependentes.

Se ainda existirem veículos associados pela FK `veiculos.numero_nota`, a exclusão é bloqueada pelo banco e a API retorna conflito.

### Respostas principais

- `204 No Content` - exclusão realizada;
- `404 Not Found` - venda inexistente;
- `409 Conflict` - existem veículos ou outros registros vinculados à venda.

---

# 5. Padrão de erros

Erros de validação de DTO retornam `400 Bad Request` com os campos inválidos.

### Exemplo

```json
{
  "valorTotalVenda": "O valor total da venda deve ser maior que zero"
}
```

Recurso inexistente:

```json
{
  "erro": "Venda não encontrada: 999999"
}
```

Duplicidade de veículo:

```json
{
  "erro": "Placa já cadastrada: RZE4J82"
}
```

Veículo já utilizado por outra venda:

```json
{
  "erro": "Veículo já vinculado a outra venda: 9BDTRANS000000001"
}
```

Conflito de integridade referencial:

```json
{
  "erro": "Não é possível concluir a operação porque existem registros vinculados"
}
```

---

# 6. Códigos HTTP utilizados

| Código | Significado |
|---|---|
| `200 OK` | consulta ou atualização realizada |
| `201 Created` | cadastro realizado |
| `204 No Content` | exclusão realizada |
| `400 Bad Request` | dados ou regras de negócio inválidos |
| `404 Not Found` | recurso principal ou relacionado não encontrado |
| `409 Conflict` | duplicidade, veículo já vinculado ou integridade referencial |

---

# 7. Observações para integração com o frontend

Os contratos JSON desta documentação correspondem aos DTOs expostos pelo backend.

No ambiente local, os endpoints CRUD estão disponíveis em:

```text
http://localhost:8080/api/...
```

A integração em Docker pode utilizar `/api` via proxy/Nginx conforme a configuração da infraestrutura do projeto.

Os identificadores utilizados pelo frontend devem seguir o banco oficial:

- Cliente: `idCliente`;
- Vendedor: `matricula`;
- Veículo: `chassi`;
- Venda: `numeroNota`.

As especializações devem ser enviadas usando:

```text
Cliente: FISICA | JURIDICA
Veículo: NOVO | USADO
```

Para vendas:

- `chassis` é uma lista de strings;
- no POST, `chassis` pode ser omitido;
- no PUT, campo omitido preserva os vínculos;
- no PUT, lista vazia remove todos os vínculos;
- no PUT, lista preenchida substitui os vínculos atuais;
- respostas de Venda incluem a lista `chassis`.

---

# 8. Validações realizadas

Os endpoints foram validados contra o MySQL oficial da Entrega 2.

Foram verificados, entre outros cenários:

- listagem e busca individual;
- cadastro;
- atualização;
- exclusão;
- cliente PF e PJ;
- veículo novo e usado;
- troca de especialização de veículo;
- chassi duplicado;
- placa duplicada;
- cliente inexistente em venda;
- vendedor inexistente em venda;
- valores inválidos;
- respostas `400`, `404` e `409`;
- bloqueio de exclusão quando existem registros vinculados;
- cadastro de venda com veículo e persistência de `veiculos.numero_nota`;
- retorno dos chassis associados em POST, GET e PUT de vendas;
- rejeição com `409 Conflict` quando um veículo já pertence a outra venda;
- rollback da venda quando o vínculo com veículo falha;
- substituição de veículo em uma venda por PUT;
- desvinculação do veículo anterior na atualização;
- remoção de todos os vínculos usando `"chassis": []`;
- limpeza dos registros temporários usados nos testes;
- testes unitários de `VendaServiceTest` e `VendaControllerTest` executados com `BUILD SUCCESS`.

---

# 9. Resumo da implementação final

O backend da Entrega 2 disponibiliza CRUD REST para:

```text
CLIENTES
VENDEDORES
VEICULOS
VENDAS
```

As especializações de clientes e veículos são tratadas de forma consistente com o DDL oficial.

A implementação de vendas inclui associação com múltiplos veículos por chassi, atualização dos vínculos e proteção contra reutilização de um veículo já associado a outra venda.

As operações que envolvem mais de uma tabela são executadas com transação para evitar persistência parcial em caso de erro.

O projeto continua utilizando o banco oficial com:

```properties
spring.jpa.hibernate.ddl-auto=none
```

Não é necessário que o Hibernate crie ou altere o esquema para o funcionamento do CRUD.
