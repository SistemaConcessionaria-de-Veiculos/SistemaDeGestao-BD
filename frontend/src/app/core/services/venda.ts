import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import {
  VendaAtualizacaoRequest,
  VendaCadastroRequest,
  VendaResponse,
} from '../models/venda';

@Injectable({
  providedIn: 'root',
})
export class VendaService {
  private readonly apiUrl =
    'http://localhost:8080/api/vendas';

  constructor(
    private readonly http: HttpClient,
  ) {}

  listar(): Observable<VendaResponse[]> {
    return this.http.get<VendaResponse[]>(
      this.apiUrl,
    );
  }

  buscar(numeroNota: number): Observable<VendaResponse> {
    return this.http.get<VendaResponse>(
      `${this.apiUrl}/${numeroNota}`,
    );
  }

  cadastrar(
    dados: VendaCadastroRequest,
  ): Observable<VendaResponse> {
    return this.http.post<VendaResponse>(
      this.apiUrl,
      dados,
    );
  }

  atualizar(
    numeroNota: number,
    dados: VendaAtualizacaoRequest,
  ): Observable<VendaResponse> {
    return this.http.put<VendaResponse>(
      `${this.apiUrl}/${numeroNota}`,
      dados,
    );
  }

  excluir(numeroNota: number): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/${numeroNota}`,
    );
  }
}