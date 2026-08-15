CREATE DATABASE IF NOT EXISTS concessionaria
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE concessionaria;

create table clientes (
    id_cliente INT UNSIGNED AUTO_INCREMENT,
    nome VARCHAR(120) NOT NULL,
    email VARCHAR(150) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    rua VARCHAR(150) NOT NULL,
    numero VARCHAR(10) NOT NULL,
    cep CHAR(8) NOT NULL,

    CONSTRAINT pk_clientes PRIMARY KEY (id_cliente)
);

create table pessoas_fisicas(
	id_cliente int unsigned not null,
    cpf char(11) not null,
    
    constraint pk_pessoas_fisicas
		primary key (id_cliente),
        
	constraint uq_pessoas_fisicas_cpf
		unique (cpf),
        
	constraint fk_pessoas_fisicas_clientes
		foreign key (id_cliente)
        references clientes (id_cliente)
);

create table pessoas_juridicas (
	id_cliente int unsigned not null,
    cnpj char(14) not null,
    
    constraint pk_pessoas_juridicas
		primary key (id_cliente),
        
	constraint uq_pessoas_juridicas_cnpj
		unique (cnpj),
	
    constraint fk_pessoas_juridicas
		foreign key (id_cliente)
        references clientes (id_cliente)
);

create table vendedores (
	matricula int unsigned not null auto_increment,
    nome varchar(120) not null,
    cpf char(11) not null,
    
    constraint pk_vendedores
		primary key (matricula),
        
	constraint uq_vendedores_cpf
		unique (cpf)
);

create table vendas(
	numero_nota bigint unsigned not null auto_increment,
    id_cliente int unsigned not null,
    matricula_vendedor int unsigned not null,
    valor_total_venda decimal(10,2) not null,
    data_da_venda date not null,
    
    constraint pk_vendas
		primary key (numero_nota),
    
    constraint fk_vendas_clientes
		foreign key (id_cliente)
        references clientes(id_cliente),
        
	constraint fk_vendas_vendedor 
		foreign key (matricula_vendedor)
        references vendedores(matricula),
	
    constraint ck_vendas_valor_total
		check (valor_total_venda > 0)
);

create table veiculos(
	chassi char(17) not null,
    numero_nota bigint unsigned not null,
    marca varchar(50) not null,
    modelo varchar(80) not null,
    cor varchar(30) not null,
    data_fabricacao date not null,
    status_disponibilidade varchar(20) not null,
    valor_veiculo decimal(12,2) not null,
    
    constraint pk_chassi
		primary key(chassi),
        
	constraint fk_nota_venda
		foreign key (numero_nota)
        references vendas(numero_nota),
        
	constraint ck_veiculos_disponibilade
		check(
			status_disponibilidade in (
				'disponivel',
                'reservado',
                'vendido'
			)
		),
        
	constraint ck_valor_veiculo
		check (valor_veiculo > 0)
);

        
create table veiculos_novos (
	chassi char(17) not null,
    
    constraint pk_chassi
		primary key (chassi),
        
	constraint fk_veiculo_novo_chassi
		foreign key (chassi)
        references veiculos(chassi)
	);

create table veiculos_usados(
	chassi char(17) not null,
    placa char(7) not null,
    quilometragem int unsigned not null,
    
    constraint pk_chassi
		primary key (chassi),
        
	constraint fk_veiculo_usado_chassi
		foreign key (chassi)
        references veiculos(chassi),
	
    constraint uq_placa
		unique (placa),
        
	constraint ck_quilometragem
		check(quilometragem>=0)
);

create table customizacoes(
	codigo int unsigned not null auto_increment,
    nome_opcional varchar(100) not null,
    valor_tabela decimal(10,2) not null,
    
    constraint pk_codigo
		primary key (codigo),
        
	constraint ck_valor_tabela
		check (valor_tabela>=0)
);

SHOW TABLES;