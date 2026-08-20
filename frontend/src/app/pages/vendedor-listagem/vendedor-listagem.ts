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
  VendedorAtualizacaoRequest,
  VendedorResponse,
} from '../../core/models/vendedor';
import { VendedorService } from '../../core/services/vendedor';

@Component({
  selector: 'app-vendedor-listagem',
  imports: [
    CommonModule,
    FormsModule,
    Navbar,
  ],
  templateUrl: './vendedor-listagem.html',
  styleUrl: './vendedor-listagem.css',
})
export class VendedorListagem implements OnInit {
  vendedores: VendedorResponse[] = [];

  carregando = false;
  salvando = false;
  excluindoMatricula: number | null = null;

  mensagemErro = '';
  mensagemSucesso = '';

  matriculaEdicao: number | null = null;

  nome = '';
  cpf = '';

  constructor(
    private readonly vendedorService: VendedorService,
    private readonly changeDetector: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.listarVendedores();
  }

  listarVendedores(): void {
    this.carregando = true;
    this.mensagemErro = '';

    this.vendedorService.listar().subscribe({
      next: (response) => {
        this.vendedores = response;
        this.carregando = false;

        this.changeDetector.detectChanges();
      },

      error: (error: HttpErrorResponse) => {
        this.carregando = false;

        this.mensagemErro = this.obterMensagemErro(
          error,
          'Não foi possível carregar os vendedores.',
        );

        this.changeDetector.detectChanges();
      },
    });
  }

  iniciarEdicao(matricula: number): void {
    this.mensagemErro = '';
    this.mensagemSucesso = '';

    this.vendedorService.buscar(matricula).subscribe({
      next: (vendedor) => {
        this.matriculaEdicao = vendedor.matricula;
        this.nome = vendedor.nome;
        this.cpf = vendedor.cpf;

        this.changeDetector.detectChanges();
      },

      error: (error: HttpErrorResponse) => {
        this.mensagemErro = this.obterMensagemErro(
          error,
          'Não foi possível carregar o vendedor.',
        );

        this.changeDetector.detectChanges();
      },
    });
  }

  salvarEdicao(formulario: NgForm): void {
    this.mensagemErro = '';
    this.mensagemSucesso = '';

    if (
      this.matriculaEdicao === null ||
      formulario.invalid ||
      !/^\d{11}$/.test(this.cpf)
    ) {
      formulario.control.markAllAsTouched();

      this.mensagemErro =
        'Informe nome e CPF com 11 dígitos.';

      return;
    }

    const dados: VendedorAtualizacaoRequest = {
      nome: this.nome.trim(),
      cpf: this.cpf.trim(),
    };

    this.salvando = true;

    this.vendedorService
      .atualizar(this.matriculaEdicao, dados)
      .subscribe({
        next: () => {
          this.salvando = false;

          this.mensagemSucesso =
            'Vendedor atualizado com sucesso.';

          this.cancelarEdicao();
          this.listarVendedores();
        },

        error: (error: HttpErrorResponse) => {
          this.salvando = false;

          this.mensagemErro =
            this.obterMensagemErro(
              error,
              'Não foi possível atualizar o vendedor.',
            );

          this.changeDetector.detectChanges();
        },
      });
  }

  excluir(vendedor: VendedorResponse): void {
    const confirmado = window.confirm(
      `Deseja realmente excluir o vendedor ${vendedor.nome}?`,
    );

    if (!confirmado) {
      return;
    }

    this.excluindoMatricula = vendedor.matricula;
    this.mensagemErro = '';
    this.mensagemSucesso = '';

    this.vendedorService
      .excluir(vendedor.matricula)
      .subscribe({
        next: () => {
          this.excluindoMatricula = null;

          this.mensagemSucesso =
            'Vendedor excluído com sucesso.';

          this.listarVendedores();
        },

        error: (error: HttpErrorResponse) => {
          this.excluindoMatricula = null;

          this.mensagemErro =
            this.obterMensagemErro(
              error,
              'Não foi possível excluir o vendedor.',
            );

          this.changeDetector.detectChanges();
        },
      });
  }

  cancelarEdicao(): void {
    this.matriculaEdicao = null;
    this.nome = '';
    this.cpf = '';
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