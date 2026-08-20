import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import {
  ChangeDetectorRef,
  Component,
  OnInit,
} from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';

import { Navbar } from '../../components/navbar/navbar';
import {
  StatusVeiculo,
  TipoVeiculo,
  VeiculoAtualizacaoRequest,
  VeiculoListagemResponse,
} from '../../core/models/veiculo';
import { VeiculoService } from '../../core/services/veiculo';

@Component({
  selector: 'app-veiculo-listagem',
  imports: [
    CommonModule,
    FormsModule,
    Navbar,
  ],
  templateUrl: './veiculo-listagem.html',
  styleUrl: './veiculo-listagem.css',
})
export class VeiculoListagem implements OnInit {
  veiculos: VeiculoListagemResponse[] = [];

  carregando = false;
  salvando = false;
  excluindoChassi: string | null = null;

  mensagemErro = '';
  mensagemSucesso = '';

  chassiEdicao: string | null = null;

  numeroNota: number | null = null;
  marca = '';
  modelo = '';
  cor = '';
  dataFabricacao = '';
  statusDisponibilidade: StatusVeiculo = 'disponivel';
  valorVeiculo: number | null = null;
  tipo: TipoVeiculo = 'NOVO';
  placa = '';
  quilometragem: number | null = null;

  constructor(
    private readonly veiculoService: VeiculoService,
    private readonly changeDetector: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.listarVeiculos();
  }

  listarVeiculos(): void {
    this.carregando = true;
    this.mensagemErro = '';

    this.veiculoService.listar().subscribe({
      next: (response) => {
        this.veiculos = response;
        this.carregando = false;
        this.changeDetector.detectChanges();
      },

      error: (error: HttpErrorResponse) => {
        this.carregando = false;
        this.mensagemErro = this.obterMensagemErro(
          error,
          'Não foi possível carregar os veículos.',
        );
        this.changeDetector.detectChanges();
      },
    });
  }

  iniciarEdicao(chassi: string): void {
    this.mensagemErro = '';
    this.mensagemSucesso = '';

    this.veiculoService.buscar(chassi).subscribe({
      next: (veiculo) => {
        this.chassiEdicao = veiculo.chassi;
        this.numeroNota = veiculo.numeroNota;
        this.marca = veiculo.marca;
        this.modelo = veiculo.modelo;
        this.cor = veiculo.cor;
        this.dataFabricacao = veiculo.dataFabricacao;
        this.statusDisponibilidade =
          veiculo.statusDisponibilidade;
        this.valorVeiculo = veiculo.valorVeiculo;
        this.tipo = veiculo.tipo;
        this.placa = veiculo.placa ?? '';
        this.quilometragem =
          veiculo.quilometragem ?? null;

        this.changeDetector.detectChanges();
      },

      error: (error: HttpErrorResponse) => {
        this.mensagemErro = this.obterMensagemErro(
          error,
          'Não foi possível carregar o veículo.',
        );
        this.changeDetector.detectChanges();
      },
    });
  }

  salvarEdicao(formulario: NgForm): void {
    if (
      !this.chassiEdicao ||
      formulario.invalid ||
      !this.dataFabricacao ||
      this.valorVeiculo === null ||
      this.valorVeiculo <= 0
    ) {
      formulario.control.markAllAsTouched();
      this.mensagemErro =
        'Preencha os campos corretamente.';
      return;
    }

    if (
      this.tipo === 'USADO' &&
      (
        this.placa.trim().length !== 7 ||
        this.quilometragem === null ||
        this.quilometragem < 0
      )
    ) {
      this.mensagemErro =
        'Veículos usados devem possuir placa com 7 caracteres e quilometragem válida.';
      return;
    }

    const dados: VeiculoAtualizacaoRequest = {
      numeroNota: this.numeroNota,
      marca: this.marca.trim(),
      modelo: this.modelo.trim(),
      cor: this.cor.trim(),
      dataFabricacao: this.dataFabricacao,
      statusDisponibilidade: this.statusDisponibilidade,
      valorVeiculo: this.valorVeiculo,
      tipo: this.tipo,
      placa:
        this.tipo === 'USADO'
          ? this.placa.trim().toUpperCase()
          : null,
      quilometragem:
        this.tipo === 'USADO'
          ? this.quilometragem
          : null,
    };

    this.salvando = true;
    this.mensagemErro = '';
    this.mensagemSucesso = '';

    this.veiculoService
      .atualizar(this.chassiEdicao, dados)
      .subscribe({
        next: () => {
          this.salvando = false;
          this.mensagemSucesso =
            'Veículo atualizado com sucesso.';

          this.cancelarEdicao();
          this.listarVeiculos();
        },

        error: (error: HttpErrorResponse) => {
          this.salvando = false;
          this.mensagemErro = this.obterMensagemErro(
            error,
            'Não foi possível atualizar o veículo.',
          );
          this.changeDetector.detectChanges();
        },
      });
  }

  excluir(veiculo: VeiculoListagemResponse): void {
    const confirmado = window.confirm(
      `Deseja realmente excluir o veículo ${veiculo.marca} ${veiculo.modelo} - ${veiculo.chassi}?`,
    );

    if (!confirmado) {
      return;
    }

    this.excluindoChassi = veiculo.chassi;
    this.mensagemErro = '';
    this.mensagemSucesso = '';

    this.veiculoService.excluir(veiculo.chassi).subscribe({
      next: () => {
        this.excluindoChassi = null;
        this.mensagemSucesso =
          'Veículo excluído com sucesso.';

        this.listarVeiculos();
      },

      error: (error: HttpErrorResponse) => {
        this.excluindoChassi = null;
        this.mensagemErro = this.obterMensagemErro(
          error,
          'Não foi possível excluir o veículo.',
        );
        this.changeDetector.detectChanges();
      },
    });
  }

  cancelarEdicao(): void {
    this.chassiEdicao = null;
    this.numeroNota = null;
    this.marca = '';
    this.modelo = '';
    this.cor = '';
    this.dataFabricacao = '';
    this.statusDisponibilidade = 'disponivel';
    this.valorVeiculo = null;
    this.tipo = 'NOVO';
    this.placa = '';
    this.quilometragem = null;
  }

  alterarTipo(): void {
    if (this.tipo === 'NOVO') {
      this.placa = '';
      this.quilometragem = null;
    }
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