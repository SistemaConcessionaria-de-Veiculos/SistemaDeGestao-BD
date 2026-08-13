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

A configuração atualmente utilizada pela aplicação é:

* **SGBD:** MySQL 8
* **Nome do banco:** `concessionaria`
* **Host:** `localhost`
* **Porta:** `3306`
* **Usuário:** `root`
* **Senha:** configurada por meio da variável de ambiente `DB_PASSWORD`

A senha do banco de dados não é armazenada diretamente no código-fonte. A variável de ambiente `DB_PASSWORD` deve ser configurada no ambiente em que a aplicação for executada.

A configuração definitiva utilizada pelo ambiente Docker será documentada após a conclusão da etapa de conteinerização.

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

O esquema relacional será analisado de acordo com as formas normais, atendendo, no mínimo, aos requisitos da Segunda Forma Normal (2FN).

A análise e a justificativa da normalização serão adicionadas nesta seção após a conclusão dessa etapa.

**Situação atual:** em desenvolvimento.

---

## 9. Implementação Física do Banco de Dados

A implementação física será realizada por meio de um script SQL DDL responsável pela criação da estrutura do banco de dados.

O script contemplará:

* criação das tabelas;
* definição das chaves primárias;
* definição das chaves estrangeiras;
* restrições de integridade;
* índices necessários.

O arquivo será disponibilizado no diretório `sql` do repositório.

**Situação atual:** em desenvolvimento.

---

## 10. Povoamento do Banco de Dados

O banco de dados será povoado com dados plausíveis para permitir a realização de testes e consultas.

O povoamento respeitará o volume mínimo de registros estabelecido para as tabelas principais e secundárias.

A metodologia utilizada para geração e inserção dos dados será documentada nesta seção após a conclusão dessa etapa.

**Situação atual:** em desenvolvimento.

---

## 11. Ambiente de Execução com Docker

O ambiente de banco de dados será disponibilizado utilizando Docker.

O arquivo `docker-compose.yml` será responsável pela configuração do serviço do MySQL e pela inicialização do banco de dados.

Ao final da implementação, o ambiente deverá permitir que a estrutura do banco seja criada e que os dados sejam carregados de forma automatizada.

**Situação atual:** em desenvolvimento.

---

## 12. Execução Atual da Aplicação

Enquanto o ambiente Docker específico da entrega de Banco de Dados não estiver concluído, a aplicação pode ser executada utilizando as ferramentas instaladas localmente.

### Pré-requisitos

São necessários:

* Java 21;
* Node.js e npm;
* MySQL Server 8;
* Git.

### Backend

A partir da raiz do repositório, acesse o diretório `backend`.

No Windows, o backend pode ser iniciado com:

`.\mvnw.cmd spring-boot:run`

O backend utiliza, por padrão, a porta `8080`.

Antes da execução, a variável de ambiente `DB_PASSWORD` deve conter a senha do usuário MySQL configurado.

### Frontend

A partir da raiz do repositório, acesse o diretório `frontend`.

As dependências podem ser instaladas com:

`npm install`

Em seguida, a aplicação pode ser iniciada com:

`npm start`

O frontend utiliza, por padrão, a porta `4200`.

---

## 13. Organização das Entregas

As etapas do projeto de Banco de Dados serão versionadas no repositório e disponibilizadas por meio de Releases no GitHub.

Cada Release corresponderá ao estado do projeto no momento de uma determinada entrega parcial.
