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
  ClienteAtualizacaoRequest,
  ClienteResponse,
  TipoCliente,
} from '../../core/models/cliente';
import { ClienteService } from '../../core/services/cliente';

@Component({
  selector: 'app-cliente-listagem',
  imports: [
    CommonModule,
    FormsModule,
    Navbar,
  ],
  templateUrl: './cliente-listagem.html',
  styleUrl: './cliente-listagem.css',
})
export class ClienteListagem implements OnInit {
  clientes: ClienteResponse[] = [];

  carregando = false;
  salvando = false;
  excluindoId: number | null = null;

  mensagemErro = '';
  mensagemSucesso = '';

  idClienteEdicao: number | null = null;

  nome = '';
  email = '';
  telefone = '';
  rua = '';
  numero = '';
  cep = '';
  tipo: TipoCliente = 'FISICA';
  cpf = '';
  cnpj = '';

  constructor(
    private readonly clienteService: ClienteService,
    private readonly changeDetector: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.listarClientes();
  }

  listarClientes(): void {
    this.carregando = true;
    this.mensagemErro = '';

    this.clienteService.listar().subscribe({
      next: (response) => {
        this.clientes = response;
        this.carregando = false;

        this.changeDetector.detectChanges();
      },

      error: (error: HttpErrorResponse) => {
        this.carregando = false;

        this.mensagemErro = this.obterMensagemErro(
          error,
          'Não foi possível carregar os clientes.',
        );

        this.changeDetector.detectChanges();
      },
    });
  }

  iniciarEdicao(idCliente: number): void {
    this.mensagemErro = '';
    this.mensagemSucesso = '';

    this.clienteService.buscar(idCliente).subscribe({
      next: (cliente) => {
        this.idClienteEdicao = cliente.idCliente;

        this.nome = cliente.nome;
        this.email = cliente.email;
        this.telefone = cliente.telefone;
        this.rua = cliente.rua;
        this.numero = cliente.numero;
        this.cep = cliente.cep;
        this.tipo = cliente.tipo;

        this.cpf = cliente.cpf ?? '';
        this.cnpj = cliente.cnpj ?? '';

        this.changeDetector.detectChanges();
      },

      error: (error: HttpErrorResponse) => {
        this.mensagemErro = this.obterMensagemErro(
          error,
          'Não foi possível carregar o cliente.',
        );

        this.changeDetector.detectChanges();
      },
    });
  }

  salvarEdicao(formulario: NgForm): void {
    this.mensagemErro = '';
    this.mensagemSucesso = '';

    if (
      this.idClienteEdicao === null ||
      formulario.invalid
    ) {
      formulario.control.markAllAsTouched();

      this.mensagemErro =
        'Preencha os campos corretamente.';

      return;
    }

    if (!/^\d{8}$/.test(this.cep)) {
      this.mensagemErro =
        'O CEP deve conter exatamente 8 dígitos.';

      return;
    }

    if (
      this.tipo === 'FISICA' &&
      !/^\d{11}$/.test(this.cpf)
    ) {
      this.mensagemErro =
        'O CPF deve conter exatamente 11 dígitos.';

      return;
    }

    if (
      this.tipo === 'JURIDICA' &&
      !/^\d{14}$/.test(this.cnpj)
    ) {
      this.mensagemErro =
        'O CNPJ deve conter exatamente 14 dígitos.';

      return;
    }

    const dados: ClienteAtualizacaoRequest = {
      nome: this.nome.trim(),
      email: this.email.trim(),
      telefone: this.telefone.trim(),
      rua: this.rua.trim(),
      numero: this.numero.trim(),
      cep: this.cep.trim(),
      tipo: this.tipo,

      cpf:
        this.tipo === 'FISICA'
          ? this.cpf.trim()
          : null,

      cnpj:
        this.tipo === 'JURIDICA'
          ? this.cnpj.trim()
          : null,
    };

    this.salvando = true;

    this.clienteService
      .atualizar(this.idClienteEdicao, dados)
      .subscribe({
        next: () => {
          this.salvando = false;

          this.mensagemSucesso =
            'Cliente atualizado com sucesso.';

          this.cancelarEdicao();
          this.listarClientes();
        },

        error: (error: HttpErrorResponse) => {
          this.salvando = false;

          this.mensagemErro =
            this.obterMensagemErro(
              error,
              'Não foi possível atualizar o cliente.',
            );

          this.changeDetector.detectChanges();
        },
      });
  }

  excluir(cliente: ClienteResponse): void {
    const confirmado = window.confirm(
      `Deseja realmente excluir o cliente ${cliente.nome}?`,
    );

    if (!confirmado) {
      return;
    }

    this.excluindoId = cliente.idCliente;

    this.mensagemErro = '';
    this.mensagemSucesso = '';

    this.clienteService
      .excluir(cliente.idCliente)
      .subscribe({
        next: () => {
          this.excluindoId = null;

          this.mensagemSucesso =
            'Cliente excluído com sucesso.';

          this.listarClientes();
        },

        error: (error: HttpErrorResponse) => {
          this.excluindoId = null;

          this.mensagemErro =
            this.obterMensagemErro(
              error,
              'Não foi possível excluir o cliente.',
            );

          this.changeDetector.detectChanges();
        },
      });
  }

  alterarTipo(): void {
    if (this.tipo === 'FISICA') {
      this.cnpj = '';
    } else {
      this.cpf = '';
    }
  }

  cancelarEdicao(): void {
    this.idClienteEdicao = null;

    this.nome = '';
    this.email = '';
    this.telefone = '';
    this.rua = '';
    this.numero = '';
    this.cep = '';
    this.tipo = 'FISICA';
    this.cpf = '';
    this.cnpj = '';
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