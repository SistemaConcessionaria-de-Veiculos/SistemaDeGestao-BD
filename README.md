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

O esquema relacional do Sistema de Gestão para Concessionária de Veículos foi analisado em relação às formas normais com o objetivo de reduzir redundâncias, evitar anomalias de inserção, atualização e exclusão e manter a consistência dos dados.

### 8.1 Primeira Forma Normal (1FN)

Todas as relações do esquema atendem à Primeira Forma Normal.

Os atributos armazenam valores atômicos, não existindo grupos repetitivos ou atributos multivalorados armazenados em uma única coluna.

No caso da tabela `CLIENTES`, o endereço foi decomposto nos atributos `rua`, `numero` e `cep`, evitando o armazenamento de uma estrutura composta em um único atributo.

Da mesma forma, o relacionamento entre veículos e customizações não é armazenado como uma lista dentro da tabela `VEICULOS`. Essa relação é representada pela tabela associativa `VEICULO_CUSTOMIZACAO`, permitindo registrar individualmente cada associação entre um veículo e uma customização.

Dessa forma, o esquema atende aos requisitos da 1FN.

### 8.2 Segunda Forma Normal (2FN)

Para atender à Segunda Forma Normal, uma relação deve estar na Primeira Forma Normal e todos os seus atributos não-chave devem possuir dependência funcional completa em relação à chave primária.

As tabelas `CLIENTES`, `PESSOAS_FISICAS`, `PESSOAS_JURIDICAS`, `VENDEDORES`, `VENDAS`, `VEICULOS`, `VEICULOS_NOVOS`, `VEICULOS_USADOS` e `CUSTOMIZACOES` possuem chaves primárias formadas por apenas um atributo. Por esse motivo, não apresentam dependências parciais de chave.

A tabela `VEICULO_CUSTOMIZACAO` possui chave primária composta pelos atributos `chassi` e `codigo_customizacao`.

Seu atributo não-chave, `preco_aplicado`, depende da combinação completa desses dois atributos, pois representa o preço de uma determinada customização aplicada a um veículo específico.

Assim, não existe dependência de `preco_aplicado` apenas em `chassi` ou apenas em `codigo_customizacao`.

Consequentemente, todas as relações do esquema atendem à Segunda Forma Normal.

### 8.3 Terceira Forma Normal (3FN)

Embora o requisito mínimo da entrega seja a Segunda Forma Normal, o esquema também foi analisado em relação à Terceira Forma Normal.

Não foram identificadas dependências transitivas entre atributos não-chave nas relações definidas pelo modelo.

Informações referentes a entidades distintas permanecem armazenadas em suas respectivas tabelas e são relacionadas por meio de chaves estrangeiras.

Por exemplo, a tabela `VENDAS` armazena apenas os identificadores `id_cliente` e `matricula_vendedor` para estabelecer os relacionamentos com `CLIENTES` e `VENDEDORES`. Dados como nome, CPF e demais informações dessas entidades não são repetidos na tabela `VENDAS`.

Da mesma forma, a tabela `VEICULO_CUSTOMIZACAO` utiliza as chaves estrangeiras `chassi` e `codigo_customizacao`, sem repetir os demais dados existentes nas tabelas `VEICULOS` e `CUSTOMIZACOES`.

Com base nas dependências funcionais definidas pelo modelo, o esquema encontra-se normalizado até a Terceira Forma Normal (3FN), atendendo, portanto, ao requisito mínimo de normalização estabelecido para a entrega.

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

O arquivo [docker-compose.yml](docker-compose.yml) configura o serviço do MySQL e inicializa o banco em um contêiner Docker.

### Configuração do banco
- SGBD: MySQL 8
- Banco: `concessionaria`
- Usuário: `concessionaria`
- Senha: `concessionaria123`
- Porta: `3306`

### Como executar
No diretório raiz do projeto, execute:

```bash
docker compose up -d
```

Para verificar se o container está em execução:

```bash
docker compose ps
```

Para parar o ambiente:

```bash
docker compose down
```

### Estrutura de inicialização
Os scripts SQL de criação e povoamento do banco ficam na pasta `init-scripts` e são executados automaticamente pelo MySQL ao subir o container.

Arquivos esperados:
- `01-create-schema.sql`
- `02-seed.sql`

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
