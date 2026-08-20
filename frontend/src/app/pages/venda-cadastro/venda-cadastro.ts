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
import { VendaCadastroRequest } from '../../core/models/venda';
import { VendedorResponse } from '../../core/models/vendedor';

import { ClienteService } from '../../core/services/cliente';
import { VeiculoService } from '../../core/services/veiculo';
import { VendaService } from '../../core/services/venda';
import { VendedorService } from '../../core/services/vendedor';

@Component({
  selector: 'app-venda-cadastro',
  imports: [
    CommonModule,
    FormsModule,
    Navbar,
  ],
  templateUrl: './venda-cadastro.html',
  styleUrl: './venda-cadastro.css',
})
export class VendaCadastro implements OnInit {
  clientes: ClienteResponse[] = [];
  vendedores: VendedorResponse[] = [];
  veiculos: VeiculoListagemResponse[] = [];

  idCliente: number | null = null;
  matriculaVendedor: number | null = null;
  valorTotalVenda: number | null = null;
  dataDaVenda = '';

  chassisSelecionados: string[] = [];

  carregandoDados = false;
  enviando = false;

  mensagemErro = '';
  mensagemSucesso = '';

  constructor(
    private readonly clienteService: ClienteService,
    private readonly vendedorService: VendedorService,
    private readonly veiculoService: VeiculoService,
    private readonly vendaService: VendaService,
    private readonly changeDetector: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.carregarDados();
  }

  carregarDados(): void {
    this.carregandoDados = true;
    this.mensagemErro = '';

    this.clienteService.listar().subscribe({
      next: (clientes) => {
        this.clientes = clientes;

        this.vendedorService.listar().subscribe({
          next: (vendedores) => {
            this.vendedores = vendedores;

            this.veiculoService.listar().subscribe({
              next: (veiculos) => {
                this.veiculos = veiculos.filter(
                  (veiculo) =>
                    veiculo.statusDisponibilidade !== 'vendido',
                );

                this.carregandoDados = false;
                this.changeDetector.detectChanges();
              },

              error: (error: HttpErrorResponse) => {
                this.carregandoDados = false;

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
            this.carregandoDados = false;

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
        this.carregandoDados = false;

        this.mensagemErro =
          this.obterMensagemErro(
            error,
            'Não foi possível carregar os clientes.',
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

  cadastrar(formulario: NgForm): void {
    this.mensagemErro = '';
    this.mensagemSucesso = '';

    if (
      formulario.invalid ||
      this.idCliente === null ||
      this.matriculaVendedor === null ||
      this.valorTotalVenda === null ||
      this.valorTotalVenda <= 0 ||
      !this.dataDaVenda
    ) {
      formulario.control.markAllAsTouched();

      this.mensagemErro =
        'Preencha os campos obrigatórios corretamente.';

      return;
    }

    const dados: VendaCadastroRequest = {
      idCliente: this.idCliente,
      matriculaVendedor: this.matriculaVendedor,
      valorTotalVenda: this.valorTotalVenda,
      dataDaVenda: this.dataDaVenda,
      chassis: [...this.chassisSelecionados],
    };

    this.enviando = true;

    this.vendaService.cadastrar(dados).subscribe({
      next: () => {
        this.enviando = false;

        this.mensagemErro = '';
        this.mensagemSucesso =
          'Venda cadastrada com sucesso.';

        formulario.resetForm({
          idCliente: null,
          matriculaVendedor: null,
          valorTotalVenda: null,
          dataDaVenda: '',
        });

        this.idCliente = null;
        this.matriculaVendedor = null;
        this.valorTotalVenda = null;
        this.dataDaVenda = '';
        this.chassisSelecionados = [];

        this.carregarDados();

        this.changeDetector.detectChanges();
      },

      error: (error: HttpErrorResponse) => {
        this.enviando = false;

        this.mensagemErro =
          this.obterMensagemErro(
            error,
            'Não foi possível cadastrar a venda.',
          );

        this.changeDetector.detectChanges();
      },
    });
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