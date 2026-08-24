package com.concessionaria.backend.repository;

import com.concessionaria.backend.dto.relatorio.ClientesComprasResponse;
import com.concessionaria.backend.dto.relatorio.ResumoVendasResponse;
import com.concessionaria.backend.dto.relatorio.VeiculosCustomizacoesResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RelatorioRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private ResultSet resultado;

    private RelatorioRepository relatorioRepository;

    @BeforeEach
    void configurar() {
        relatorioRepository = new RelatorioRepository(jdbcTemplate);
    }

    @Test
    void deveBuscarResumoVendas() throws Exception {
        when(resultado.getLong("numero_nota")).thenReturn(81L);
        when(resultado.getDate("data_da_venda"))
                .thenReturn(Date.valueOf(LocalDate.of(2026, 8, 18)));
        when(resultado.getInt("id_cliente")).thenReturn(12);
        when(resultado.getString("nome_cliente"))
                .thenReturn("Mariana Albuquerque");
        when(resultado.getInt("matricula_vendedor")).thenReturn(7);
        when(resultado.getString("nome_vendedor"))
                .thenReturn("Rafael Montenegro");
        when(resultado.getLong("quantidade_veiculos")).thenReturn(2L);
        when(resultado.getBigDecimal("valor_total_venda"))
                .thenReturn(new BigDecimal("245900.00"));

        configurarMapeamentoDoJdbcTemplate();

        List<ResumoVendasResponse> resposta =
                relatorioRepository.buscarResumoVendas();

        assertThat(resposta).containsExactly(new ResumoVendasResponse(
                81L,
                LocalDate.of(2026, 8, 18),
                12,
                "Mariana Albuquerque",
                7,
                "Rafael Montenegro",
                2L,
                new BigDecimal("245900.00")
        ));
        assertThat(capturarSqlExecutado())
                .contains("from v_resumo_vendas")
                .contains("order by data_da_venda, numero_nota");
    }

    @Test
    void deveBuscarClientesCompras() throws Exception {
        when(resultado.getInt("id_cliente")).thenReturn(12);
        when(resultado.getString("nome_cliente"))
                .thenReturn("Mariana Albuquerque");
        when(resultado.getLong("quantidade_compras")).thenReturn(3L);
        when(resultado.getLong("quantidade_veiculos")).thenReturn(4L);
        when(resultado.getBigDecimal("valor_total_compras"))
                .thenReturn(new BigDecimal("438700.00"));

        configurarMapeamentoDoJdbcTemplate();

        List<ClientesComprasResponse> resposta =
                relatorioRepository.buscarClientesCompras();

        assertThat(resposta).containsExactly(new ClientesComprasResponse(
                12,
                "Mariana Albuquerque",
                3L,
                4L,
                new BigDecimal("438700.00")
        ));
        assertThat(capturarSqlExecutado())
                .contains("from v_clientes_compras")
                .contains("order by nome_cliente");
    }

    @Test
    void deveBuscarVeiculosCustomizacoes() throws Exception {
        when(resultado.getString("chassi"))
                .thenReturn("9BWZZZ377VT004251");
        when(resultado.getString("marca")).thenReturn("Volkswagen");
        when(resultado.getString("modelo")).thenReturn("Nivus");
        when(resultado.getString("status_disponibilidade"))
                .thenReturn("reservado");
        when(resultado.getString("tipo_veiculo")).thenReturn("USADO");
        when(resultado.getString("placa")).thenReturn("BRA2E26");
        when(resultado.getObject("quilometragem", Integer.class))
                .thenReturn(28500);
        when(resultado.getBigDecimal("valor_veiculo"))
                .thenReturn(new BigDecimal("119900.00"));
        when(resultado.getLong("quantidade_customizacoes"))
                .thenReturn(1L);
        when(resultado.getString("customizacoes"))
                .thenReturn("Central Multimidia");
        when(resultado.getBigDecimal("custo_customizacoes"))
                .thenReturn(new BigDecimal("3200.00"));
        when(resultado.getBigDecimal("valor_total_com_customizacoes"))
                .thenReturn(new BigDecimal("123100.00"));

        configurarMapeamentoDoJdbcTemplate();

        List<VeiculosCustomizacoesResponse> resposta =
                relatorioRepository.buscarVeiculosCustomizacoes();

        assertThat(resposta).containsExactly(
                new VeiculosCustomizacoesResponse(
                        "9BWZZZ377VT004251",
                        "Volkswagen",
                        "Nivus",
                        "reservado",
                        "USADO",
                        "BRA2E26",
                        28500,
                        new BigDecimal("119900.00"),
                        1L,
                        "Central Multimidia",
                        new BigDecimal("3200.00"),
                        new BigDecimal("123100.00")
                )
        );
        assertThat(capturarSqlExecutado())
                .contains("from v_veiculos_customizacoes")
                .contains("order by marca, modelo, chassi");
    }

    @SuppressWarnings("unchecked")
    private void configurarMapeamentoDoJdbcTemplate() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
                .thenAnswer(invocacao -> {
                    RowMapper<Object> rowMapper = invocacao.getArgument(1);
                    return List.of(rowMapper.mapRow(resultado, 0));
                });
    }

    @SuppressWarnings("unchecked")
    private String capturarSqlExecutado() {
        ArgumentCaptor<String> sqlCaptor =
                ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<RowMapper<?>> rowMapperCaptor =
                ArgumentCaptor.forClass(RowMapper.class);

        org.mockito.Mockito.verify(jdbcTemplate)
                .query(sqlCaptor.capture(), rowMapperCaptor.capture());

        return sqlCaptor.getValue();
    }
}
