use concessionaria;
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

create or replace view v_resumo_vendas as
select
    v.numero_nota,
    v.data_da_venda,
    c.id_cliente,
    c.nome as nome_cliente,
    vd.matricula as matricula_vendedor,
    vd.nome as nome_vendedor,
    count(ve.chassi) as quantidade_veiculos,
    v.valor_total_venda
from vendas v
inner join clientes c
    on c.id_cliente = v.id_cliente
inner join vendedores vd
    on vd.matricula = v.matricula_vendedor
left join veiculos ve
    on ve.numero_nota = v.numero_nota
group by
    v.numero_nota,
    v.data_da_venda,
    c.id_cliente,
    c.nome,
    vd.matricula,
    vd.nome,
    v.valor_total_venda;

create or replace view v_clientes_compras as
select
    c.id_cliente,
    c.nome as nome_cliente,
    count(rv.numero_nota) as quantidade_compras,
    coalesce(sum(rv.quantidade_veiculos), 0) as quantidade_veiculos,
    coalesce(sum(rv.valor_total_venda), 0.00) as valor_total_compras
from clientes c
left join (
    select
        v.numero_nota,
        v.id_cliente,
        v.valor_total_venda,
        count(ve.chassi) as quantidade_veiculos
    from vendas v
    left join veiculos ve
        on ve.numero_nota = v.numero_nota
    group by
        v.numero_nota,
        v.id_cliente,
        v.valor_total_venda
) rv
    on rv.id_cliente = c.id_cliente
group by
    c.id_cliente,
    c.nome;

create or replace view v_veiculos_customizacoes as
select
    v.chassi,
    v.marca,
    v.modelo,
    v.status_disponibilidade,

    case
        when vn.chassi is not null then 'NOVO'
        when vu.chassi is not null then 'USADO'
        else 'NAO_CLASSIFICADO'
    end as tipo_veiculo,

    vu.placa,
    vu.quilometragem,
    v.valor_veiculo,

    count(vc.codigo_customizacao) as quantidade_customizacoes,

    coalesce(
        group_concat(
            c.nome_opcional
            order by c.nome_opcional
            separator ', '
        ),
        'Sem customização'
    ) as customizacoes,

    coalesce(sum(vc.preco_aplicado), 0.00) as custo_customizacoes,

    v.valor_veiculo
        + coalesce(sum(vc.preco_aplicado), 0.00)
        as valor_total_com_customizacoes

from veiculos v

left join veiculos_novos vn
    on vn.chassi = v.chassi

left join veiculos_usados vu
    on vu.chassi = v.chassi

left join veiculo_customizacao vc
    on vc.chassi = v.chassi

left join customizacoes c
    on c.codigo = vc.codigo_customizacao

group by
    v.chassi,
    v.marca,
    v.modelo,
    v.status_disponibilidade,
    vn.chassi,
    vu.chassi,
    vu.placa,
    vu.quilometragem,
    v.valor_veiculo;