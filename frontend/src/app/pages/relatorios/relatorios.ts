import { CommonModule } from '@angular/common';
import {
  ChangeDetectorRef,
  Component,
  OnInit,
} from '@angular/core';

import { jsPDF } from 'jspdf';
import autoTable from 'jspdf-autotable';

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

  // =========================
  // EXPORTAÇÃO CSV
  // =========================

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

  // =========================
  // EXPORTAÇÃO PDF
  // =========================

  exportarResumoVendasPdf(): void {
    const doc = new jsPDF({
      orientation: 'landscape',
      unit: 'mm',
      format: 'a4',
    });

    this.adicionarCabecalhoPdf(
      doc,
      'Relatório - Resumo de Vendas',
      'Dados consolidados das vendas realizadas.',
    );

    autoTable(doc, {
      startY: 34,

      head: [[
        'Nota',
        'Data',
        'Cliente',
        'Vendedor',
        'Veículos',
        'Valor Total',
      ]],

      body: this.resumoVendas.map((venda) => [
        `#${venda.numeroNota}`,
        this.formatarData(venda.dataDaVenda),
        venda.nomeCliente,
        venda.nomeVendedor,
        venda.quantidadeVeiculos,
        this.formatarMoeda(venda.valorTotalVenda),
      ]),

      styles: {
        fontSize: 8,
        cellPadding: 2.5,
      },

      headStyles: {
        fontStyle: 'bold',
      },

      margin: {
        left: 12,
        right: 12,
      },

      didDrawPage: () => {
        this.adicionarRodapePdf(doc);
      },
    });

    doc.save('relatorio-resumo-vendas.pdf');
  }

  exportarClientesComprasPdf(): void {
    const doc = new jsPDF({
      orientation: 'portrait',
      unit: 'mm',
      format: 'a4',
    });

    this.adicionarCabecalhoPdf(
      doc,
      'Relatório - Clientes e Compras',
      'Consolidado de compras realizadas por cliente.',
    );

    autoTable(doc, {
      startY: 34,

      head: [[
        'Cliente',
        'Compras',
        'Veículos',
        'Valor Total',
      ]],

      body: this.clientesCompras.map((cliente) => [
        cliente.nomeCliente,
        cliente.quantidadeCompras,
        cliente.quantidadeVeiculos,
        this.formatarMoeda(cliente.valorTotalCompras),
      ]),

      styles: {
        fontSize: 8,
        cellPadding: 2.5,
      },

      headStyles: {
        fontStyle: 'bold',
      },

      margin: {
        left: 12,
        right: 12,
      },

      didDrawPage: () => {
        this.adicionarRodapePdf(doc);
      },
    });

    doc.save('relatorio-clientes-compras.pdf');
  }

  exportarVeiculosCustomizacoesPdf(): void {
    const doc = new jsPDF({
      orientation: 'landscape',
      unit: 'mm',
      format: 'a4',
    });

    this.adicionarCabecalhoPdf(
      doc,
      'Relatório - Veículos e Customizações',
      'Valores dos veículos e customizações associadas.',
    );

    autoTable(doc, {
      startY: 34,

      head: [[
        'Veículo',
        'Tipo',
        'Status',
        'Placa / Km',
        'Customizações',
        'Valor',
        'Opcionais',
        'Total',
      ]],

      body: this.veiculosCustomizacoes.map((veiculo) => [
        `${veiculo.marca} ${veiculo.modelo}\n${veiculo.chassi}`,

        veiculo.tipoVeiculo,

        veiculo.statusDisponibilidade,

        veiculo.tipoVeiculo === 'USADO'
          ? `${veiculo.placa ?? '-'} / ${
              veiculo.quilometragem ?? '-'
            } km`
          : 'Veículo novo',

        veiculo.customizacoes || 'Nenhuma',

        this.formatarMoeda(veiculo.valorVeiculo),

        this.formatarMoeda(veiculo.custoCustomizacoes),

        this.formatarMoeda(
          veiculo.valorTotalComCustomizacoes,
        ),
      ]),

      styles: {
        fontSize: 7,
        cellPadding: 2,
        overflow: 'linebreak',
      },

      headStyles: {
        fontStyle: 'bold',
      },

      columnStyles: {
        0: {
          cellWidth: 42,
        },

        1: {
          cellWidth: 18,
        },

        2: {
          cellWidth: 21,
        },

        3: {
          cellWidth: 27,
        },

        4: {
          cellWidth: 58,
        },

        5: {
          cellWidth: 29,
        },

        6: {
          cellWidth: 29,
        },

        7: {
          cellWidth: 29,
        },
      },

      margin: {
        left: 8,
        right: 8,
      },

      didDrawPage: () => {
        this.adicionarRodapePdf(doc);
      },
    });

    doc.save('relatorio-veiculos-customizacoes.pdf');
  }

  private adicionarCabecalhoPdf(
    doc: jsPDF,
    titulo: string,
    descricao: string,
  ): void {
    doc.setFont('helvetica', 'bold');
    doc.setFontSize(16);

    doc.text(
      'Sistema de Gestão para Concessionária de Veículos',
      12,
      14,
    );

    doc.setFontSize(13);

    doc.text(
      titulo,
      12,
      22,
    );

    doc.setFont('helvetica', 'normal');
    doc.setFontSize(9);

    doc.text(
      descricao,
      12,
      28,
    );
  }

  private adicionarRodapePdf(
    doc: jsPDF,
  ): void {
    const quantidadePaginas =
      doc.getNumberOfPages();

    const larguraPagina =
      doc.internal.pageSize.getWidth();

    const alturaPagina =
      doc.internal.pageSize.getHeight();

    doc.setFont('helvetica', 'normal');
    doc.setFontSize(8);

    doc.text(
      `Gerado em ${new Date().toLocaleString('pt-BR')}`,
      12,
      alturaPagina - 7,
    );

    doc.text(
      `Página ${quantidadePaginas}`,
      larguraPagina - 12,
      alturaPagina - 7,
      {
        align: 'right',
      },
    );
  }

  // =========================
  // FUNÇÕES AUXILIARES
  // =========================

  private exportarCsv(
    nomeArquivo: string,
    cabecalhos: string[],
    linhas: Array<Array<string | number>>,
  ): void {
    const separador = ';';

    const conteudo = [
      cabecalhos
        .map((item) => this.escaparCsv(item))
        .join(separador),

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