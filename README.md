# Sistema de Gestão para Concessionária de Veículos

## 1. Apresentação

Este repositório contém o projeto **Sistema de Gestão para Concessionária de Veículos**, desenvolvido para a disciplina de Banco de Dados.

O sistema tem como objetivo auxiliar no gerenciamento das principais informações de uma concessionária, permitindo organizar e manipular dados relacionados a clientes, vendedores, veículos, vendas e customizações.

A aplicação possui integração entre banco de dados, backend e frontend, disponibilizando operações CRUD por meio de uma API REST e consultas gerenciais baseadas em Views SQL.

---

## 2. Integrantes

- Ricardo Matias de Lima
- Luana Siqueira de Sousa
- Rianna de Queiroz Tenório Vaz
- Bianca Maria Cardoso das Neves

---

## 3. Contexto do Projeto

O projeto consiste no desenvolvimento de um sistema de gerenciamento para uma concessionária de veículos.

O banco de dados centraliza as informações necessárias para o funcionamento da aplicação, contemplando clientes, vendedores, veículos, vendas e customizações.

Entre as principais funcionalidades do sistema estão:

- cadastro, consulta, edição e exclusão de clientes;
- classificação de clientes como pessoa física ou pessoa jurídica;
- cadastro, consulta, edição e exclusão de vendedores;
- cadastro, consulta, edição e exclusão de veículos;
- classificação de veículos como novos ou usados;
- controle da disponibilidade dos veículos;
- cadastro, consulta, edição e exclusão de vendas;
- associação das vendas aos respectivos clientes e vendedores;
- associação dos veículos às vendas;
- cadastro de opcionais e customizações;
- associação de customizações aos veículos;
- registro do preço aplicado às customizações;
- consultas gerenciais utilizando Views SQL;
- apresentação das informações das Views em uma tela de Relatórios no frontend.

Os veículos podem apresentar três estados de disponibilidade:

- `disponivel`;
- `reservado`;
- `vendido`.

---

## 4. Tecnologias Utilizadas

### Backend

- Java 21
- Spring Boot 3.2.5
- Spring Data JPA
- Hibernate
- Spring Security
- Maven
- API REST

### Frontend

- Angular
- TypeScript
- Node.js
- npm
- Nginx

### Banco de Dados

- MySQL 8
- SQL
- Views SQL

### Infraestrutura

- Docker
- Docker Compose

---

## 5. Arquitetura da Aplicação

A aplicação está organizada em três serviços principais:

- **Banco de Dados:** MySQL 8;
- **Backend:** aplicação Spring Boot responsável pela API REST, regras da aplicação e comunicação com o banco de dados;
- **Frontend:** aplicação Angular responsável pela interface utilizada pelo usuário e disponibilizada por meio do Nginx.

Os três serviços são integrados utilizando Docker Compose.

### Portas utilizadas

| Serviço | Porta |
|---|---:|
| Frontend | `4200` |
| Backend | `8080` |
| MySQL | `3306` |

---

## 6. Configuração do Banco de Dados

A configuração utilizada pelo ambiente Docker é:

- **SGBD:** MySQL 8
- **Nome do banco:** `concessionaria`
- **Porta externa:** `3306`
- **Usuário:** `concessionaria`
- **Senha:** `concessionaria123`

Dentro da rede Docker, o backend acessa o banco de dados por meio do serviço `db`.

O arquivo responsável pela configuração e integração dos serviços é:

- `docker-compose.yml`

---

## 7. Modelo Lógico Relacional

O Modelo Lógico Relacional foi elaborado a partir da transformação do Modelo Entidade-Relacionamento Estendido desenvolvido para o sistema.

O diagrama apresenta as tabelas, atributos, tipos de dados, chaves primárias, chaves estrangeiras, restrições, relacionamentos, cardinalidades e especializações utilizadas no banco de dados.

O documento está disponível em:

- [Modelo Lógico Relacional em PDF](docs/modelo_logico.pdf)

O modelo possui especializações disjuntas entre:

- `CLIENTES`, `PESSOAS_FISICAS` e `PESSOAS_JURIDICAS`;
- `VEICULOS`, `VEICULOS_NOVOS` e `VEICULOS_USADOS`.

---

## 8. Dicionário de Dados

O Dicionário de Dados apresenta a documentação detalhada da estrutura lógica do banco de dados.

O documento contém, para cada tabela, sua finalidade e, para cada atributo, o respectivo tipo de dado, restrições aplicadas e significado.

Também são documentadas as chaves primárias, chaves estrangeiras, especializações, cardinalidades e a chave primária composta da tabela associativa `VEICULO_CUSTOMIZACAO`.

O documento completo está disponível em:

- [Dicionário de Dados em PDF](docs/dicionario_de_dados.pdf)

---

## 9. Normalização

O esquema relacional do Sistema de Gestão para Concessionária de Veículos foi analisado com o objetivo de reduzir redundâncias, evitar anomalias de inserção, atualização e exclusão e manter a consistência dos dados.

A normalização foi verificada até a Segunda Forma Normal (2FN), conforme o requisito estabelecido para a entrega.

### 9.1 Primeira Forma Normal (1FN)

Todas as relações do esquema atendem à Primeira Forma Normal.

Os atributos armazenam valores atômicos, não existindo grupos repetitivos ou atributos multivalorados armazenados em uma única coluna.

Na tabela `CLIENTES`, o endereço foi representado por atributos distintos, como:

- `rua`;
- `numero`;
- `cep`.

O relacionamento entre veículos e customizações também não é armazenado como uma lista dentro da tabela `VEICULOS`.

Essa relação é representada pela tabela associativa `VEICULO_CUSTOMIZACAO`.

### 9.2 Segunda Forma Normal (2FN)

As tabelas:

- `CLIENTES`;
- `PESSOAS_FISICAS`;
- `PESSOAS_JURIDICAS`;
- `VENDEDORES`;
- `VENDAS`;
- `VEICULOS`;
- `VEICULOS_NOVOS`;
- `VEICULOS_USADOS`;
- `CUSTOMIZACOES`;

possuem chaves primárias formadas por um único atributo.

Dessa forma, não existe possibilidade de dependência parcial nessas tabelas.

A tabela `VEICULO_CUSTOMIZACAO` possui chave primária composta pelos atributos:

- `chassi`;
- `codigo_customizacao`.

O atributo `preco_aplicado` depende da combinação completa dessa chave, pois representa o preço de uma customização aplicada a um veículo específico.

### 9.3 Conclusão da Normalização

Todas as relações do esquema atendem à Primeira Forma Normal (1FN) e à Segunda Forma Normal (2FN).

A tabela `VEICULO_CUSTOMIZACAO`, por possuir uma chave primária composta, foi analisada especificamente quanto à existência de dependências parciais.

Não foram identificadas dependências parciais no esquema.

---

## 10. Implementação Física do Banco de Dados

A implementação física do banco de dados foi realizada por meio do script:

- `init-scripts/01-create-schema.sql`

Esse arquivo é responsável pela criação das tabelas, chaves primárias, chaves estrangeiras e demais restrições do banco.

As principais tabelas criadas são:

- `CLIENTES`
- `PESSOAS_FISICAS`
- `PESSOAS_JURIDICAS`
- `VENDEDORES`
- `VENDAS`
- `VEICULOS`
- `VEICULOS_NOVOS`
- `VEICULOS_USADOS`
- `CUSTOMIZACOES`
- `VEICULO_CUSTOMIZACAO`

O Hibernate está configurado com:

```properties
spring.jpa.hibernate.ddl-auto=none
```

Dessa forma, a estrutura oficial do banco permanece definida pelos scripts SQL presentes no projeto.

---

## 11. Povoamento do Banco de Dados

O povoamento inicial é realizado por meio do script:

- `init-scripts/02-seed.sql`

Os dados são sintéticos e representam informações plausíveis para o contexto de uma concessionária.

A carga inicial contém:

- 70 registros em `CLIENTES`;
- 50 registros em `PESSOAS_FISICAS`;
- 20 registros em `PESSOAS_JURIDICAS`;
- 50 registros em `VENDEDORES`;
- 50 registros em `VENDAS`;
- 60 registros em `VEICULOS`;
- 30 registros em `VEICULOS_NOVOS`;
- 30 registros em `VEICULOS_USADOS`;
- 70 registros em `CUSTOMIZACOES`;
- 60 registros em `VEICULO_CUSTOMIZACAO`.

Os scripts utilizam a codificação `utf8mb4` para preservar corretamente caracteres acentuados.

---

## 12. CRUD das Principais Entidades

O sistema disponibiliza operações de criação, consulta, edição e exclusão das principais entidades utilizadas na aplicação.

Foram implementados CRUDs para:

- clientes;
- vendedores;
- veículos;
- vendas.

O frontend realiza as operações por meio da API REST disponibilizada pelo backend.

As operações permitem cadastrar novos registros, visualizar os dados existentes, alterar informações e excluir registros respeitando as restrições existentes no banco de dados.

---

## 13. Views SQL

Para atender às consultas gerenciais da segunda entrega, foram implementadas três Views SQL não triviais.

As Views são criadas pelo arquivo:

- `init-scripts/03-create-views.sql`

### 13.1 `v_resumo_vendas`

A View `v_resumo_vendas` apresenta informações consolidadas relacionadas às vendas.

Entre os dados disponibilizados estão:

- número da nota;
- data da venda;
- identificação do cliente;
- nome do cliente;
- matrícula do vendedor;
- nome do vendedor;
- quantidade de veículos;
- valor total da venda.

### 13.2 `v_clientes_compras`

A View `v_clientes_compras` apresenta informações consolidadas das compras realizadas pelos clientes.

Entre os dados disponibilizados estão:

- identificação do cliente;
- nome do cliente;
- quantidade de compras;
- quantidade de veículos adquiridos;
- valor total das compras.

### 13.3 `v_veiculos_customizacoes`

A View `v_veiculos_customizacoes` apresenta informações referentes aos veículos e suas respectivas customizações.

Entre os dados disponibilizados estão:

- chassi;
- marca;
- modelo;
- status de disponibilidade;
- tipo do veículo;
- placa;
- quilometragem;
- valor do veículo;
- quantidade de customizações;
- descrição das customizações;
- custo das customizações;
- valor total do veículo com as customizações.

---

## 14. API REST

O backend disponibiliza uma API REST utilizada pelo frontend para realizar as operações do sistema.

As operações CRUD principais contemplam:

- clientes;
- vendedores;
- veículos;
- vendas.

Também foram criados endpoints específicos para as consultas gerenciais baseadas nas Views SQL.

### 14.1 API de Relatórios

Os endpoints de relatórios são:

```text
GET /api/relatorios/resumo-vendas
GET /api/relatorios/clientes-compras
GET /api/relatorios/veiculos-customizacoes
```

Esses endpoints consultam as Views existentes no MySQL e retornam as informações utilizadas pela tela de Relatórios do frontend.

---

## 15. Frontend

O frontend foi desenvolvido utilizando Angular e possui telas para gerenciamento das principais entidades do sistema.

Entre as telas disponíveis estão:

- Dashboard;
- Clientes;
- Cadastro de cliente;
- Vendedores;
- Cadastro de vendedor;
- Veículos;
- Cadastro de veículo;
- Vendas;
- Cadastro de venda;
- Relatórios.

As telas de clientes, vendedores, veículos e vendas permitem realizar as operações de cadastro, consulta, edição e exclusão.

A aplicação possui uma barra de navegação que permite acessar as principais funcionalidades do sistema.

---

## 16. Tela de Relatórios

A aplicação possui uma tela específica para apresentação das consultas gerenciais.

Essa tela consome os três endpoints disponibilizados pela API de relatórios e apresenta os dados provenientes das três Views SQL em tabelas distintas.

São apresentados:

- **Resumo de Vendas**;
- **Clientes e Compras**;
- **Veículos e Customizações**.

A tela também permite atualizar os dados exibidos por meio da opção **Atualizar dados**.

Dessa forma, as informações das três Views podem ser consultadas diretamente pela interface da aplicação.

---

## 17. Inicialização Automática do Banco

Na primeira criação do volume MySQL, os scripts presentes na pasta `init-scripts` são executados automaticamente.

A ordem de execução é:

1. `01-create-schema.sql` — criação da estrutura do banco de dados;
2. `02-seed.sql` — povoamento inicial;
3. `03-create-views.sql` — criação das Views utilizadas nos relatórios.

Essa execução ocorre automaticamente durante a inicialização do container MySQL.

---

## 18. Execução Completa com Docker

### 18.1 Pré-requisitos

Para executar a aplicação completa é necessário possuir:

- Docker;
- Docker Compose;
- Git, caso o projeto seja obtido diretamente pelo repositório.

Para a execução completa por meio do Docker não é necessário instalar Java, Maven, Node.js ou Angular localmente.

Essas dependências são utilizadas durante a construção das imagens dos respectivos serviços.

### 18.2 Inicialização

Na raiz do projeto, execute:

```bash
docker compose up --build
```

O Docker Compose realizará o build e a inicialização dos três serviços:

- MySQL;
- backend Spring Boot;
- frontend Angular com Nginx.

Após a inicialização, a aplicação pode ser acessada em:

```text
http://localhost:4200
```

O backend fica disponível em:

```text
http://localhost:8080
```

O MySQL fica disponível na porta:

```text
3306
```

### 18.3 Verificação dos containers

Para verificar o estado dos serviços, utilize:

```bash
docker compose ps
```

Os serviços de backend e frontend devem permanecer em execução, enquanto o banco de dados deve apresentar o status `healthy`.

### 18.4 Encerramento

Para encerrar os containers mantendo o volume e os dados do banco:

```bash
docker compose down
```

Para encerrar os containers e também remover o volume do banco:

```bash
docker compose down -v
```

Ao remover o volume, os scripts SQL serão executados novamente durante a próxima inicialização do banco.

---

## 19. Execução para Desenvolvimento

Também é possível executar os componentes separadamente durante o desenvolvimento.

### 19.1 Banco de Dados

Na raiz do projeto:

```bash
docker compose up -d db
```

### 19.2 Backend

Acesse a pasta:

```text
backend
```

No Windows, execute:

```powershell
.\mvnw.cmd spring-boot:run
```

O backend utiliza a porta:

```text
8080
```

### 19.3 Frontend

Acesse a pasta:

```text
frontend
```

Instale as dependências:

```bash
npm install
```

Depois execute:

```bash
npm start
```

O frontend utiliza a porta:

```text
4200
```

---

## 20. Estrutura Principal do Projeto

```text
SistemaDeGestao-BD/
├── backend/
├── frontend/
├── init-scripts/
│   ├── 01-create-schema.sql
│   ├── 02-seed.sql
│   └── 03-create-views.sql
├── docs/
├── docker-compose.yml
└── README.md
```

### Diretório `backend`

Contém a aplicação Spring Boot responsável pela API REST e comunicação com o banco de dados.

### Diretório `frontend`

Contém a aplicação Angular responsável pela interface do sistema.

### Diretório `init-scripts`

Contém os scripts utilizados para criação, povoamento e definição das Views do banco de dados.

### Diretório `docs`

Contém os documentos relacionados à modelagem e documentação do projeto.

---

## 21. Entrega 2

A segunda entrega do projeto contempla:

- CRUD completo das principais entidades no backend;
- CRUD das principais entidades no frontend;
- comunicação entre frontend e backend por meio de API REST;
- três Views SQL não triviais;
- endpoints específicos para consulta das Views;
- tela de Relatórios apresentando informações provenientes das três Views;
- banco de dados MySQL integrado à aplicação;
- backend Spring Boot integrado ao banco;
- frontend Angular integrado ao backend;
- execução do frontend por meio do Nginx;
- integração de banco, backend e frontend utilizando Docker Compose;
- inicialização automática da estrutura, povoamento e Views do banco;
- execução completa da aplicação por meio do comando:

```bash
docker compose up --build
```

A integração completa da aplicação foi validada utilizando um novo volume do banco de dados, permitindo verificar a inicialização automática dos scripts e o funcionamento conjunto dos três serviços.

---

## 22. Organização das Entregas

As etapas do projeto são versionadas no repositório Git.

As entregas podem ser disponibilizadas por meio de Releases no GitHub, permitindo identificar o estado do projeto correspondente a cada etapa da disciplina.