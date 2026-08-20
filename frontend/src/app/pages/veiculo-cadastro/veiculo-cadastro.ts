import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import {
  ChangeDetectorRef,
  Component,
} from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';

import { Navbar } from '../../components/navbar/navbar';
import {
  StatusVeiculo,
  TipoVeiculo,
  VeiculoCadastroRequest,
} from '../../core/models/veiculo';
import { VeiculoService } from '../../core/services/veiculo';

@Component({
  selector: 'app-veiculo-cadastro',
  imports: [CommonModule, FormsModule, Navbar],
  templateUrl: './veiculo-cadastro.html',
  styleUrl: './veiculo-cadastro.css',
})
export class VeiculoCadastro {
  chassi = '';
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

  mensagemSucesso = '';
  mensagemErro = '';
  enviando = false;

  constructor(
    private readonly veiculoService: VeiculoService,
    private readonly changeDetector: ChangeDetectorRef,
  ) {}

  cadastrar(formulario: NgForm): void {
    this.mensagemSucesso = '';
    this.mensagemErro = '';

    if (
      formulario.invalid ||
      this.chassi.trim().length !== 17 ||
      !this.dataFabricacao ||
      this.valorVeiculo === null ||
      this.valorVeiculo <= 0
    ) {
      formulario.control.markAllAsTouched();
      this.mensagemErro =
        'Preencha os campos obrigatórios corretamente.';
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

    const dados: VeiculoCadastroRequest = {
      chassi: this.chassi.trim().toUpperCase(),
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

    this.enviando = true;

    this.veiculoService.cadastrar(dados).subscribe({
      next: () => {
        this.enviando = false;
        this.mensagemErro = '';
        this.mensagemSucesso =
          'Veículo cadastrado com sucesso.';

        formulario.resetForm({
          chassi: '',
          numeroNota: null,
          marca: '',
          modelo: '',
          cor: '',
          dataFabricacao: '',
          statusDisponibilidade: 'disponivel',
          valorVeiculo: null,
          tipo: 'NOVO',
          placa: '',
          quilometragem: null,
        });

        this.changeDetector.detectChanges();
      },

      error: (error: HttpErrorResponse) => {
        this.enviando = false;
        this.mensagemErro = this.obterMensagemErro(error);
        this.changeDetector.detectChanges();
      },
    });
  }

  alterarTipo(): void {
    if (this.tipo === 'NOVO') {
      this.placa = '';
      this.quilometragem = null;
    }
  }

  private obterMensagemErro(
    error: HttpErrorResponse,
  ): string {
    if (error.status === 0) {
      return 'Não foi possível conectar ao servidor.';
    }

    if (error.status === 409) {
      return this.extrairMensagem(
        error,
        'Já existe um veículo com o chassi ou placa informados.',
      );
    }

    if (error.status === 400) {
      return this.extrairMensagem(
        error,
        'Verifique os dados informados.',
      );
    }

    if (error.status === 404) {
      return this.extrairMensagem(
        error,
        'Registro relacionado não encontrado.',
      );
    }

    return 'Não foi possível cadastrar o veículo.';
  }

  private extrairMensagem(
    error: HttpErrorResponse,
    padrao: string,
  ): string {
    if (!error.error) {
      return padrao;
    }

    if (typeof error.error === 'string') {
      return error.error;
    }

    if (
      typeof error.error === 'object' &&
      typeof error.error.erro === 'string'
    ) {
      return error.error.erro;
    }

    const mensagens = Object.values(error.error)
      .filter(
        (mensagem): mensagem is string =>
          typeof mensagem === 'string',
      )
      .join(' ');

    return mensagens || padrao;
  }
}