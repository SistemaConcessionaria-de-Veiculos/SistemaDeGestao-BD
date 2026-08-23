package com.concessionaria.backend.repository;

import com.concessionaria.backend.dto.relatorio.ClientesComprasResponse;
import com.concessionaria.backend.dto.relatorio.ResumoVendasResponse;
import com.concessionaria.backend.dto.relatorio.VeiculosCustomizacoesResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RelatorioRepository {

    private final JdbcTemplate jdbcTemplate;

    public RelatorioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ResumoVendasResponse> buscarResumoVendas() {
        String sql = """
                select *
                from v_resumo_vendas
                order by data_da_venda, numero_nota
                """;

        return jdbcTemplate.query(sql, (resultado, numeroLinha) ->
                new ResumoVendasResponse(
                        resultado.getLong("numero_nota"),
                        resultado.getDate("data_da_venda").toLocalDate(),
                        resultado.getInt("id_cliente"),
                        resultado.getString("nome_cliente"),
                        resultado.getInt("matricula_vendedor"),
                        resultado.getString("nome_vendedor"),
                        resultado.getLong("quantidade_veiculos"),
                        resultado.getBigDecimal("valor_total_venda")
                )
        );
    }

    public List<ClientesComprasResponse> buscarClientesCompras() {
        String sql = """
                select *
                from v_clientes_compras
                order by nome_cliente
                """;

        return jdbcTemplate.query(sql, (resultado, numeroLinha) ->
                new ClientesComprasResponse(
                        resultado.getInt("id_cliente"),
                        resultado.getString("nome_cliente"),
                        resultado.getLong("quantidade_compras"),
                        resultado.getLong("quantidade_veiculos"),
                        resultado.getBigDecimal("valor_total_compras")
                )
        );
    }

    public List<VeiculosCustomizacoesResponse> buscarVeiculosCustomizacoes() {
        String sql = """
                select *
                from v_veiculos_customizacoes
                order by marca, modelo, chassi
                """;

        return jdbcTemplate.query(sql, (resultado, numeroLinha) ->
                new VeiculosCustomizacoesResponse(
                        resultado.getString("chassi"),
                        resultado.getString("marca"),
                        resultado.getString("modelo"),
                        resultado.getString("status_disponibilidade"),
                        resultado.getString("tipo_veiculo"),
                        resultado.getString("placa"),
                        resultado.getObject("quilometragem", Integer.class),
                        resultado.getBigDecimal("valor_veiculo"),
                        resultado.getLong("quantidade_customizacoes"),
                        resultado.getString("customizacoes"),
                        resultado.getBigDecimal("custo_customizacoes"),
                        resultado.getBigDecimal(
                                "valor_total_com_customizacoes"
                        )
                )
        );
    }
}