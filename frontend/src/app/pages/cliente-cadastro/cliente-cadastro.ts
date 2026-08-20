import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import {
  ChangeDetectorRef,
  Component,
} from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';

import { Navbar } from '../../components/navbar/navbar';
import {
  ClienteCadastroRequest,
  TipoCliente,
} from '../../core/models/cliente';
import { ClienteService } from '../../core/services/cliente';

@Component({
  selector: 'app-cliente-cadastro',
  imports: [
    CommonModule,
    FormsModule,
    Navbar,
  ],
  templateUrl: './cliente-cadastro.html',
  styleUrl: './cliente-cadastro.css',
})
export class ClienteCadastro {
  nome = '';
  email = '';
  telefone = '';
  rua = '';
  numero = '';
  cep = '';

  tipo: TipoCliente = 'FISICA';

  cpf = '';
  cnpj = '';

  enviando = false;

  mensagemSucesso = '';
  mensagemErro = '';

  constructor(
    private readonly clienteService: ClienteService,
    private readonly changeDetector: ChangeDetectorRef,
  ) {}

  cadastrar(formulario: NgForm): void {
    this.mensagemSucesso = '';
    this.mensagemErro = '';

    if (formulario.invalid) {
      formulario.control.markAllAsTouched();

      this.mensagemErro =
        'Preencha os campos obrigatórios corretamente.';

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

    if (!/^\d{8}$/.test(this.cep)) {
      this.mensagemErro =
        'O CEP deve conter exatamente 8 dígitos.';

      return;
    }

    const dados: ClienteCadastroRequest = {
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

    this.enviando = true;

    this.clienteService.cadastrar(dados).subscribe({
      next: () => {
        this.enviando = false;

        this.mensagemErro = '';
        this.mensagemSucesso =
          'Cliente cadastrado com sucesso.';

        formulario.resetForm({
          nome: '',
          email: '',
          telefone: '',
          rua: '',
          numero: '',
          cep: '',
          tipo: 'FISICA',
          cpf: '',
          cnpj: '',
        });

        this.tipo = 'FISICA';
        this.cpf = '';
        this.cnpj = '';

        this.changeDetector.detectChanges();
      },

      error: (error: HttpErrorResponse) => {
        this.enviando = false;

        this.mensagemErro =
          this.obterMensagemErro(error);

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

  private obterMensagemErro(
    error: HttpErrorResponse,
  ): string {
    if (error.status === 0) {
      return 'Não foi possível conectar ao servidor.';
    }

    if (error.status === 409) {
      return this.extrairMensagem(
        error,
        'CPF ou CNPJ já cadastrado.',
      );
    }

    if (error.status === 400) {
      return this.extrairMensagem(
        error,
        'Verifique os dados informados.',
      );
    }

    return this.extrairMensagem(
      error,
      'Não foi possível cadastrar o cliente.',
    );
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