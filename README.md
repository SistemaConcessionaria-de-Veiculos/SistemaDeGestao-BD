# Sistema de Gestão para Concessionária de Veículos

## 1. Apresentação

Este repositório contém o projeto **Sistema de Gestão para Concessionária de Veículos**, utilizado para o desenvolvimento das atividades da disciplina de Banco de Dados.

O sistema tem como objetivo auxiliar no gerenciamento das principais informações de uma concessionária, permitindo organizar dados relacionados a clientes, vendedores, veículos, vendas, disponibilidade de veículos e customizações.

O código-base da aplicação foi inicialmente desenvolvido em outro contexto acadêmico e foi reaproveitado neste repositório para a implementação e documentação dos requisitos relacionados ao banco de dados.

---

## 2. Integrantes

* Ricardo Matias de Lima
* Luana Siqueira de Sousa
* Rianna de Queiroz Tenório Vaz
* Bianca Maria Cardoso das Neves

---

## 3. Contexto do Projeto

O projeto consiste no desenvolvimento de um sistema de gerenciamento para uma concessionária de veículos.

O banco de dados tem como finalidade centralizar e organizar as informações necessárias para o funcionamento do sistema, contemplando clientes, vendedores, veículos, vendas e customizações.

Entre as principais funcionalidades relacionadas ao banco de dados estão:

* cadastro de clientes;
* classificação de clientes como pessoa física ou pessoa jurídica;
* cadastro de vendedores;
* cadastro de veículos;
* classificação de veículos como novos ou usados;
* controle da disponibilidade dos veículos;
* registro das vendas realizadas;
* associação das vendas aos respectivos clientes e vendedores;
* associação dos veículos às vendas;
* cadastro de opcionais e customizações;
* associação de customizações aos veículos;
* registro do preço aplicado às customizações.

Os veículos podem apresentar três estados de disponibilidade: `disponivel`, `reservado` ou `vendido`.

---

## 4. Tecnologias Utilizadas

### Backend

* Java 21
* Spring Boot 3.2.5
* Spring Data JPA
* Hibernate
* Spring Security
* JWT
* Maven

### Frontend

* Angular 22
* TypeScript
* Node.js
* npm

### Banco de Dados

* MySQL 8

---

## 5. Configuração do Banco de Dados

A configuração utilizada no ambiente local e no ambiente Docker do projeto é:

* **SGBD:** MySQL 8
* **Nome do banco:** `concessionaria`
* **Host:** `localhost` (aplicação local acessando o container)
* **Porta:** `3306`
* **Usuário:** `concessionaria`
* **Senha:** `concessionaria123`

Essa configuração está alinhada com o serviço definido em [docker-compose.yml](docker-compose.yml), que cria o banco e expõe a porta `3306` para acesso local.

---

## 6. Modelo Lógico Relacional

O Modelo Lógico Relacional foi elaborado a partir da transformação do Modelo Entidade-Relacionamento Estendido desenvolvido anteriormente.

O diagrama apresenta as tabelas, atributos, tipos de dados, chaves primárias, chaves estrangeiras, restrições, relacionamentos, cardinalidades e especializações utilizadas no banco de dados.

O documento está disponível no seguinte arquivo:

* [Modelo Lógico Relacional em PDF](docs/modelo_logico.pdf)

O modelo possui especializações disjuntas entre `CLIENTES`, `PESSOAS_FISICAS` e `PESSOAS_JURIDICAS`, bem como entre `VEICULOS`, `VEICULOS_NOVOS` e `VEICULOS_USADOS`.

---

## 7. Dicionário de Dados

O Dicionário de Dados apresenta a documentação detalhada da estrutura lógica do banco de dados.

O documento contém, para cada tabela, sua finalidade e, para cada atributo, o respectivo tipo de dado, as restrições aplicadas e sua semântica.

Também são documentadas as chaves primárias, chaves estrangeiras, especializações, cardinalidades e a chave primária composta da tabela associativa `VEICULO_CUSTOMIZACAO`.

O documento completo está disponível no seguinte arquivo:

* [Dicionário de Dados em PDF](docs/dicionario_de_dados.pdf)

---
## 8. Normalização

O esquema relacional do Sistema de Gestão para Concessionária de Veículos foi analisado com o objetivo de reduzir redundâncias, evitar anomalias de inserção, atualização e exclusão e manter a consistência dos dados.

A normalização foi verificada até a Segunda Forma Normal (2FN), conforme o requisito estabelecido para a entrega.

### 8.1 Primeira Forma Normal (1FN)

Todas as relações do esquema atendem à Primeira Forma Normal.

Os atributos armazenam valores atômicos, não existindo grupos repetitivos ou atributos multivalorados armazenados em uma única coluna.

Na tabela `CLIENTES`, o endereço foi representado por atributos distintos, como `rua`, `numero` e `cep`, evitando o armazenamento de uma estrutura composta em um único campo.

Da mesma forma, o relacionamento entre veículos e customizações não é armazenado como uma lista dentro da tabela `VEICULOS`. Essa relação é representada pela tabela associativa `VEICULO_CUSTOMIZACAO`, permitindo que cada associação entre um veículo e uma customização seja registrada individualmente.

Dessa forma, todas as relações do esquema atendem aos requisitos da Primeira Forma Normal.

### 8.2 Segunda Forma Normal (2FN)

Para que uma relação esteja na Segunda Forma Normal, ela deve atender à Primeira Forma Normal e todos os atributos não-chave devem depender funcionalmente da chave primária completa, não existindo dependências parciais.

As tabelas `CLIENTES`, `PESSOAS_FISICAS`, `PESSOAS_JURIDICAS`, `VENDEDORES`, `VENDAS`, `VEICULOS`, `VEICULOS_NOVOS`, `VEICULOS_USADOS` e `CUSTOMIZACOES` possuem chaves primárias constituídas por um único atributo. Dessa forma, não há possibilidade de dependência parcial em relação à chave primária nessas tabelas.

A tabela `VEICULO_CUSTOMIZACAO` é a única relação do esquema que possui chave primária composta, formada pelos atributos `chassi` e `codigo_customizacao`.

O atributo não-chave `preco_aplicado` depende da combinação completa de `chassi` e `codigo_customizacao`, pois representa o preço aplicado a uma determinada customização em um veículo específico.

Portanto, `preco_aplicado` não depende exclusivamente de `chassi` nem exclusivamente de `codigo_customizacao`, mas da combinação dos dois atributos que compõem a chave primária.

Com base nessa análise, não foram identificadas dependências parciais nas relações do esquema.

### 8.3 Conclusão da Normalização

Todas as relações do esquema atendem à Primeira Forma Normal (1FN) e à Segunda Forma Normal (2FN).

Em especial, a tabela `VEICULO_CUSTOMIZACAO`, por possuir uma chave primária composta, foi analisada quanto à existência de dependências parciais, sendo verificado que seu atributo não-chave `preco_aplicado` depende funcionalmente da chave primária completa.

Dessa forma, o esquema relacional encontra-se normalizado até a Segunda Forma Normal (2FN), atendendo ao requisito mínimo de normalização estabelecido para a entrega.
---

## 9. Implementação Física do Banco de Dados

A implementação física do banco de dados foi realizada por meio de um script SQL DDL, responsável pela criação da estrutura relacional definida no Modelo Lógico e no Dicionário de Dados.

O arquivo utilizado é:

- `init-scripts/01-create-schema.sql`

O script contempla a criação das tabelas do sistema, chaves primárias, chaves estrangeiras, restrições `NOT NULL`, `UNIQUE` e `CHECK`, além dos índices necessários às restrições e de índices auxiliares definidos para consultas frequentes.

As tabelas criadas são:

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

O script foi validado em MySQL 8 por meio da inicialização de um banco vazio no ambiente Docker, sendo executado sem erros e resultando na criação das dez tabelas previstas no esquema lógico.

**Situação atual:** concluído.

---

## 10. Povoamento do Banco de Dados

O povoamento do banco de dados é realizado por meio do script:

- `init-scripts/02-seed.sql`

A estratégia adotada foi a utilização de comandos SQL `INSERT` contendo dados sintéticos e plausíveis para o contexto de uma concessionária de veículos.

Os dados foram preparados respeitando a ordem das dependências entre as tabelas, permitindo que os registros referenciados pelas chaves estrangeiras fossem inseridos antes das tabelas dependentes.

A ordem principal de carga utilizada no script é:

1. `CLIENTES`;
2. `PESSOAS_FISICAS`;
3. `PESSOAS_JURIDICAS`;
4. `VENDEDORES`;
5. `CUSTOMIZACOES`;
6. `VENDAS`;
7. `VEICULOS`;
8. `VEICULOS_NOVOS`;
9. `VEICULOS_USADOS`;
10. `VEICULO_CUSTOMIZACAO`.

Foram utilizados valores sintéticos para nomes, documentos, endereços, veículos, datas, valores monetários, quilometragens e customizações. Os identificadores sujeitos a restrições de unicidade, como CPF, CNPJ, placa e chassi, foram definidos de forma a não gerar duplicidades.

A carga final contém:

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

O povoamento foi testado a partir de um banco vazio no ambiente Docker. A carga foi concluída sem violações de chaves estrangeiras, restrições `UNIQUE` ou restrições `CHECK`.

Também foram verificadas regras de consistência entre os dados, incluindo a ausência de clientes simultaneamente classificados como pessoa física e pessoa jurídica, a ausência de veículos simultaneamente classificados como novos e usados e a associação coerente entre vendas e veículos.

**Situação atual:** concluído.

---

## 11. Ambiente de Execução com Docker

O banco de dados é disponibilizado para execução local utilizando Docker e Docker Compose.

O arquivo [docker-compose.yml](docker-compose.yml) configura um serviço baseado na imagem MySQL 8 e realiza automaticamente a criação e o povoamento do banco na primeira inicialização do ambiente.

### Configuração do banco

- **SGBD:** MySQL 8
- **Banco:** `concessionaria`
- **Host:** `localhost`
- **Porta:** `3306`
- **Usuário:** `concessionaria`
- **Senha:** `concessionaria123`

### Inicialização

Na raiz do repositório, execute:

`docker compose up -d`

Durante a primeira inicialização, o MySQL executa automaticamente, nesta ordem:

- `init-scripts/01-create-schema.sql`
- `init-scripts/02-seed.sql`

O primeiro arquivo cria a estrutura do banco e o segundo realiza o povoamento.

Para verificar o estado do contêiner:

`docker compose ps`

Para visualizar os logs:

`docker compose logs db`

Para encerrar o ambiente mantendo os dados armazenados no volume:

`docker compose down`

Para remover também o volume e permitir uma nova inicialização completa do banco:

`docker compose down -v`

Após a remoção do volume, o comando `docker compose up -d` executará novamente os scripts de criação e povoamento.

O ambiente foi testado a partir de um volume vazio, sendo confirmadas a criação automática das dez tabelas e a inserção dos dados definidos no script de povoamento.

**Situação atual:** concluído.

---

## 12. Execução da Aplicação

Para a entrega de Banco de Dados, a estrutura e o povoamento do banco são controlados pelos scripts SQL executados através do Docker.

### Pré-requisitos

São necessários:

- Docker e Docker Compose;
- Java 21 para execução do backend;
- Node.js e npm para execução do frontend;
- Git.

### Banco de Dados

Antes de iniciar a aplicação, execute na raiz do projeto:

`docker compose up -d`

O MySQL será disponibilizado na porta `3306`.

### Backend

A partir da raiz do repositório, acesse o diretório `backend`.

No Windows, execute:

`.\mvnw.cmd spring-boot:run`

O backend utiliza, por padrão, a porta `8080`.

A configuração do backend utiliza as mesmas credenciais definidas no ambiente Docker.

O Hibernate está configurado com:

`spring.jpa.hibernate.ddl-auto=none`

Dessa forma, a estrutura do banco de dados não é criada nem modificada automaticamente pelo Hibernate. A definição oficial do esquema permanece sob responsabilidade do arquivo `init-scripts/01-create-schema.sql`.

### Frontend

A partir da raiz do repositório, acesse o diretório `frontend`.

Instale as dependências:

`npm install`

Em seguida, execute:

`npm start`

O frontend utiliza, por padrão, a porta `4200`.

## 13. Organização das Entregas

As etapas do projeto de Banco de Dados serão versionadas no repositório e disponibilizadas por meio de Releases no GitHub.

Cada Release corresponderá ao estado do projeto no momento de uma determinada entrega parcial.
