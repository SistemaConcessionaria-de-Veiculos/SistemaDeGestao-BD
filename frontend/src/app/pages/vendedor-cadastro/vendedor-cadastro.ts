import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import {
  ChangeDetectorRef,
  Component,
} from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';

import { Navbar } from '../../components/navbar/navbar';
import { VendedorCadastroRequest } from '../../core/models/vendedor';
import { VendedorService } from '../../core/services/vendedor';

@Component({
  selector: 'app-vendedor-cadastro',
  imports: [
    CommonModule,
    FormsModule,
    Navbar,
  ],
  templateUrl: './vendedor-cadastro.html',
  styleUrl: './vendedor-cadastro.css',
})
export class VendedorCadastro {
  nome = '';
  cpf = '';

  enviando = false;
  mensagemSucesso = '';
  mensagemErro = '';

  constructor(
    private readonly vendedorService: VendedorService,
    private readonly changeDetector: ChangeDetectorRef,
  ) {}

  cadastrar(formulario: NgForm): void {
    this.mensagemSucesso = '';
    this.mensagemErro = '';

    if (
      formulario.invalid ||
      !/^\d{11}$/.test(this.cpf)
    ) {
      formulario.control.markAllAsTouched();

      this.mensagemErro =
        'Informe nome e CPF com 11 dígitos.';

      return;
    }

    const dados: VendedorCadastroRequest = {
      nome: this.nome.trim(),
      cpf: this.cpf.trim(),
    };

    this.enviando = true;

    this.vendedorService.cadastrar(dados).subscribe({
      next: () => {
        this.enviando = false;

        this.mensagemErro = '';
        this.mensagemSucesso =
          'Vendedor cadastrado com sucesso.';

        formulario.resetForm({
          nome: '',
          cpf: '',
        });

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

  private obterMensagemErro(
    error: HttpErrorResponse,
  ): string {
    if (error.status === 0) {
      return 'Não foi possível conectar ao servidor.';
    }

    if (error.status === 409) {
      return this.extrairMensagem(
        error,
        'CPF já cadastrado.',
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
      'Não foi possível cadastrar o vendedor.',
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