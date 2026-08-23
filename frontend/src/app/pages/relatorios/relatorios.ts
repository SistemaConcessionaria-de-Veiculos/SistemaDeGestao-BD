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

  private tratarErro(): void {
    this.carregando = false;
    this.mensagemErro =
      'Não foi possível carregar os relatórios.';
    this.changeDetector.detectChanges();
  }
}