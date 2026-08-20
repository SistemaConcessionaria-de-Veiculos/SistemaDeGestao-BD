import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import {
  ChangeDetectorRef,
  Component,
  OnInit,
} from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';

import { Navbar } from '../../components/navbar/navbar';

import { ClienteResponse } from '../../core/models/cliente';
import { VeiculoListagemResponse } from '../../core/models/veiculo';
import {
  VendaAtualizacaoRequest,
  VendaResponse,
} from '../../core/models/venda';
import { VendedorResponse } from '../../core/models/vendedor';

import { ClienteService } from '../../core/services/cliente';
import { VeiculoService } from '../../core/services/veiculo';
import { VendaService } from '../../core/services/venda';
import { VendedorService } from '../../core/services/vendedor';

@Component({
  selector: 'app-venda-listagem',
  imports: [
    CommonModule,
    FormsModule,
    Navbar,
  ],
  templateUrl: './venda-listagem.html',
  styleUrl: './venda-listagem.css',
})
export class VendaListagem implements OnInit {
  vendas: VendaResponse[] = [];
  clientes: ClienteResponse[] = [];
  vendedores: VendedorResponse[] = [];
  veiculos: VeiculoListagemResponse[] = [];

  carregando = false;
  salvando = false;
  excluindoNumeroNota: number | null = null;

  mensagemErro = '';
  mensagemSucesso = '';

  numeroNotaEdicao: number | null = null;

  idCliente: number | null = null;
  matriculaVendedor: number | null = null;
  valorTotalVenda: number | null = null;
  dataDaVenda = '';

  chassisSelecionados: string[] = [];

  constructor(
    private readonly vendaService: VendaService,
    private readonly clienteService: ClienteService,
    private readonly vendedorService: VendedorService,
    private readonly veiculoService: VeiculoService,
    private readonly changeDetector: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.carregarTudo();
  }

  carregarTudo(): void {
    this.carregando = true;
    this.mensagemErro = '';

    this.vendaService.listar().subscribe({
      next: (vendas) => {
        this.vendas = vendas;

        this.clienteService.listar().subscribe({
          next: (clientes) => {
            this.clientes = clientes;

            this.vendedorService.listar().subscribe({
              next: (vendedores) => {
                this.vendedores = vendedores;

                this.veiculoService.listar().subscribe({
                  next: (veiculos) => {
                    this.veiculos = veiculos;
                    this.carregando = false;

                    this.changeDetector.detectChanges();
                  },

                  error: (error: HttpErrorResponse) => {
                    this.carregando = false;

                    this.mensagemErro =
                      this.obterMensagemErro(
                        error,
                        'Não foi possível carregar os veículos.',
                      );

                    this.changeDetector.detectChanges();
                  },
                });
              },

              error: (error: HttpErrorResponse) => {
                this.carregando = false;

                this.mensagemErro =
                  this.obterMensagemErro(
                    error,
                    'Não foi possível carregar os vendedores.',
                  );

                this.changeDetector.detectChanges();
              },
            });
          },

          error: (error: HttpErrorResponse) => {
            this.carregando = false;

            this.mensagemErro =
              this.obterMensagemErro(
                error,
                'Não foi possível carregar os clientes.',
              );

            this.changeDetector.detectChanges();
          },
        });
      },

      error: (error: HttpErrorResponse) => {
        this.carregando = false;

        this.mensagemErro =
          this.obterMensagemErro(
            error,
            'Não foi possível carregar as vendas.',
          );

        this.changeDetector.detectChanges();
      },
    });
  }

  iniciarEdicao(numeroNota: number): void {
    this.mensagemErro = '';
    this.mensagemSucesso = '';

    this.vendaService.buscar(numeroNota).subscribe({
      next: (venda) => {
        this.numeroNotaEdicao = venda.numeroNota;
        this.idCliente = venda.idCliente;
        this.matriculaVendedor =
          venda.matriculaVendedor;
        this.valorTotalVenda =
          venda.valorTotalVenda;
        this.dataDaVenda =
          venda.dataDaVenda;

        this.chassisSelecionados = [
          ...venda.chassis,
        ];

        this.changeDetector.detectChanges();
      },

      error: (error: HttpErrorResponse) => {
        this.mensagemErro =
          this.obterMensagemErro(
            error,
            'Não foi possível carregar a venda.',
          );

        this.changeDetector.detectChanges();
      },
    });
  }

  alternarChassi(
    chassi: string,
    marcado: boolean,
  ): void {
    if (marcado) {
      if (!this.chassisSelecionados.includes(chassi)) {
        this.chassisSelecionados.push(chassi);
      }

      return;
    }

    this.chassisSelecionados =
      this.chassisSelecionados.filter(
        (item) => item !== chassi,
      );
  }

  veiculoDisponivelParaEdicao(
    veiculo: VeiculoListagemResponse,
  ): boolean {
    if (
      this.chassisSelecionados.includes(
        veiculo.chassi,
      )
    ) {
      return true;
    }

    return (
      veiculo.statusDisponibilidade !== 'vendido'
    );
  }

  salvarEdicao(formulario: NgForm): void {
    this.mensagemErro = '';
    this.mensagemSucesso = '';

    if (
      this.numeroNotaEdicao === null ||
      this.idCliente === null ||
      this.matriculaVendedor === null ||
      this.valorTotalVenda === null ||
      this.valorTotalVenda <= 0 ||
      !this.dataDaVenda ||
      formulario.invalid
    ) {
      formulario.control.markAllAsTouched();

      this.mensagemErro =
        'Preencha os campos obrigatórios corretamente.';

      return;
    }

    const dados: VendaAtualizacaoRequest = {
      idCliente: this.idCliente,
      matriculaVendedor:
        this.matriculaVendedor,
      valorTotalVenda:
        this.valorTotalVenda,
      dataDaVenda:
        this.dataDaVenda,
      chassis: [
        ...this.chassisSelecionados,
      ],
    };

    this.salvando = true;

    this.vendaService
      .atualizar(this.numeroNotaEdicao, dados)
      .subscribe({
        next: () => {
          this.salvando = false;

          this.mensagemSucesso =
            'Venda atualizada com sucesso.';

          this.cancelarEdicao();
          this.carregarTudo();
        },

        error: (error: HttpErrorResponse) => {
          this.salvando = false;

          this.mensagemErro =
            this.obterMensagemErro(
              error,
              'Não foi possível atualizar a venda.',
            );

          this.changeDetector.detectChanges();
        },
      });
  }

  excluir(venda: VendaResponse): void {
    const confirmado = window.confirm(
      `Deseja realmente excluir a venda #${venda.numeroNota}?`,
    );

    if (!confirmado) {
      return;
    }

    this.excluindoNumeroNota =
      venda.numeroNota;

    this.mensagemErro = '';
    this.mensagemSucesso = '';

    this.vendaService
      .excluir(venda.numeroNota)
      .subscribe({
        next: () => {
          this.excluindoNumeroNota = null;

          this.mensagemSucesso =
            'Venda excluída com sucesso.';

          this.carregarTudo();
        },

        error: (error: HttpErrorResponse) => {
          this.excluindoNumeroNota = null;

          this.mensagemErro =
            this.obterMensagemErro(
              error,
              'Não foi possível excluir a venda.',
            );

          this.changeDetector.detectChanges();
        },
      });
  }

  cancelarEdicao(): void {
    this.numeroNotaEdicao = null;

    this.idCliente = null;
    this.matriculaVendedor = null;
    this.valorTotalVenda = null;
    this.dataDaVenda = '';
    this.chassisSelecionados = [];
  }

  nomeCliente(idCliente: number): string {
    const cliente =
      this.clientes.find(
        (item) =>
          item.idCliente === idCliente,
      );

    return cliente
      ? cliente.nome
      : `Cliente #${idCliente}`;
  }

  nomeVendedor(
    matricula: number,
  ): string {
    const vendedor =
      this.vendedores.find(
        (item) =>
          item.matricula === matricula,
      );

    return vendedor
      ? vendedor.nome
      : `Vendedor #${matricula}`;
  }

  private obterMensagemErro(
    error: HttpErrorResponse,
    padrao: string,
  ): string {
    if (error.status === 0) {
      return 'Não foi possível conectar ao servidor.';
    }

    if (
      error.error &&
      typeof error.error === 'object' &&
      typeof error.error.erro === 'string'
    ) {
      return error.error.erro;
    }

    if (typeof error.error === 'string') {
      return error.error;
    }

    if (
      error.error &&
      typeof error.error === 'object'
    ) {
      const mensagens = Object.values(error.error)
        .filter(
          (mensagem): mensagem is string =>
            typeof mensagem === 'string',
        )
        .join(' ');

      if (mensagens) {
        return mensagens;
      }
    }

    return padrao;
  }
}