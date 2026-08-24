import { CommonModule } from '@angular/common';
import {
  ChangeDetectorRef,
  Component,
  OnInit,
} from '@angular/core';

import { Navbar } from '../../components/navbar/navbar';

import {
  ClientesComprasResponse,
  ResumoVendasResponse,
  VeiculosCustomizacoesResponse,
} from '../../core/models/relatorio';

import { RelatorioService } from '../../core/services/relatorio';

@Component({
  selector: 'app-relatorios',
  imports: [
    CommonModule,
    Navbar,
  ],
  templateUrl: './relatorios.html',
  styleUrl: './relatorios.css',
})
export class Relatorios implements OnInit {
  resumoVendas: ResumoVendasResponse[] = [];
  clientesCompras: ClientesComprasResponse[] = [];
  veiculosCustomizacoes: VeiculosCustomizacoesResponse[] = [];

  carregando = true;
  mensagemErro = '';

  constructor(
    private readonly relatorioService: RelatorioService,
    private readonly changeDetector: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.carregarRelatorios();
  }

  carregarRelatorios(): void {
    this.carregando = true;
    this.mensagemErro = '';

    this.relatorioService
      .listarResumoVendas()
      .subscribe({
        next: (dados) => {
          this.resumoVendas = dados;
          this.carregarClientesCompras();
        },
        error: () => {
          this.tratarErro();
        },
      });
  }

  private carregarClientesCompras(): void {
    this.relatorioService
      .listarClientesCompras()
      .subscribe({
        next: (dados) => {
          this.clientesCompras = dados;
          this.carregarVeiculosCustomizacoes();
        },
        error: () => {
          this.tratarErro();
        },
      });
  }

  private carregarVeiculosCustomizacoes(): void {
    this.relatorioService
      .listarVeiculosCustomizacoes()
      .subscribe({
        next: (dados) => {
          this.veiculosCustomizacoes = dados;
          this.carregando = false;
          this.changeDetector.detectChanges();
        },
        error: () => {
          this.tratarErro();
        },
      });
  }

  exportarResumoVendasCsv(): void {
    const cabecalhos = [
      'Nota',
      'Data',
      'ID Cliente',
      'Cliente',
      'Matrícula Vendedor',
      'Vendedor',
      'Quantidade de Veículos',
      'Valor Total',
    ];

    const linhas = this.resumoVendas.map((venda) => [
      venda.numeroNota,
      this.formatarData(venda.dataDaVenda),
      venda.idCliente,
      venda.nomeCliente,
      venda.matriculaVendedor,
      venda.nomeVendedor,
      venda.quantidadeVeiculos,
      this.formatarMoeda(venda.valorTotalVenda),
    ]);

    this.exportarCsv(
      'relatorio-resumo-vendas.csv',
      cabecalhos,
      linhas,
    );
  }

  exportarClientesComprasCsv(): void {
    const cabecalhos = [
      'ID Cliente',
      'Cliente',
      'Quantidade de Compras',
      'Quantidade de Veículos',
      'Valor Total das Compras',
    ];

    const linhas = this.clientesCompras.map((cliente) => [
      cliente.idCliente,
      cliente.nomeCliente,
      cliente.quantidadeCompras,
      cliente.quantidadeVeiculos,
      this.formatarMoeda(cliente.valorTotalCompras),
    ]);

    this.exportarCsv(
      'relatorio-clientes-compras.csv',
      cabecalhos,
      linhas,
    );
  }

  exportarVeiculosCustomizacoesCsv(): void {
    const cabecalhos = [
      'Chassi',
      'Marca',
      'Modelo',
      'Status',
      'Tipo',
      'Placa',
      'Quilometragem',
      'Valor do Veículo',
      'Quantidade de Customizações',
      'Customizações',
      'Custo das Customizações',
      'Valor Total com Customizações',
    ];

    const linhas = this.veiculosCustomizacoes.map((veiculo) => [
      veiculo.chassi,
      veiculo.marca,
      veiculo.modelo,
      veiculo.statusDisponibilidade,
      veiculo.tipoVeiculo,
      veiculo.placa ?? '-',
      veiculo.quilometragem ?? '-',
      this.formatarMoeda(veiculo.valorVeiculo),
      veiculo.quantidadeCustomizacoes,
      veiculo.customizacoes,
      this.formatarMoeda(veiculo.custoCustomizacoes),
      this.formatarMoeda(
        veiculo.valorTotalComCustomizacoes,
      ),
    ]);

    this.exportarCsv(
      'relatorio-veiculos-customizacoes.csv',
      cabecalhos,
      linhas,
    );
  }

  private exportarCsv(
    nomeArquivo: string,
    cabecalhos: string[],
    linhas: Array<Array<string | number>>,
  ): void {
    const separador = ';';

    const conteudo = [
      cabecalhos.map((item) => this.escaparCsv(item)).join(separador),
      ...linhas.map((linha) =>
        linha
          .map((item) => this.escaparCsv(item))
          .join(separador),
      ),
    ].join('\r\n');

    const blob = new Blob(
      ['\uFEFF' + conteudo],
      {
        type: 'text/csv;charset=utf-8;',
      },
    );

    const url = URL.createObjectURL(blob);

    const link = document.createElement('a');

    link.href = url;
    link.download = nomeArquivo;

    document.body.appendChild(link);

    link.click();
    link.remove();

    URL.revokeObjectURL(url);
  }

  private escaparCsv(
    valor: string | number,
  ): string {
    const texto = String(valor ?? '');

    return `"${texto.replace(/"/g, '""')}"`;
  }

  private formatarMoeda(
    valor: number,
  ): string {
    return new Intl.NumberFormat(
      'pt-BR',
      {
        style: 'currency',
        currency: 'BRL',
      },
    ).format(valor);
  }

  private formatarData(
    data: string,
  ): string {
    if (!data) {
      return '';
    }

    const partes = data.split('-');

    if (partes.length !== 3) {
      return data;
    }

    return `${partes[2]}/${partes[1]}/${partes[0]}`;
  }

  private tratarErro(): void {
    this.carregando = false;

    this.mensagemErro =
      'Não foi possível carregar os relatórios.';

    this.changeDetector.detectChanges();
  }
}