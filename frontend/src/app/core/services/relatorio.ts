import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import {
  ClientesComprasResponse,
  ResumoVendasResponse,
  VeiculosCustomizacoesResponse,
} from '../models/relatorio';

@Injectable({
  providedIn: 'root',
})
export class RelatorioService {
  private readonly apiUrl =
    'http://localhost:8080/api/relatorios';

  constructor(
    private readonly http: HttpClient,
  ) {}

  listarResumoVendas(): Observable<ResumoVendasResponse[]> {
    return this.http.get<ResumoVendasResponse[]>(
      `${this.apiUrl}/resumo-vendas`,
    );
  }

  listarClientesCompras(): Observable<ClientesComprasResponse[]> {
    return this.http.get<ClientesComprasResponse[]>(
      `${this.apiUrl}/clientes-compras`,
    );
  }

  listarVeiculosCustomizacoes(): Observable<VeiculosCustomizacoesResponse[]> {
    return this.http.get<VeiculosCustomizacoesResponse[]>(
      `${this.apiUrl}/veiculos-customizacoes`,
    );
  }
}