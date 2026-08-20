import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import {
  ClienteAtualizacaoRequest,
  ClienteCadastroRequest,
  ClienteResponse,
} from '../models/cliente';

@Injectable({
  providedIn: 'root',
})
export class ClienteService {
  private readonly apiUrl =
    'http://localhost:8080/api/clientes';

  constructor(
    private readonly http: HttpClient,
  ) {}

  listar(): Observable<ClienteResponse[]> {
    return this.http.get<ClienteResponse[]>(
      this.apiUrl,
    );
  }

  buscar(idCliente: number): Observable<ClienteResponse> {
    return this.http.get<ClienteResponse>(
      `${this.apiUrl}/${idCliente}`,
    );
  }

  cadastrar(
    dados: ClienteCadastroRequest,
  ): Observable<ClienteResponse> {
    return this.http.post<ClienteResponse>(
      this.apiUrl,
      dados,
    );
  }

  atualizar(
    idCliente: number,
    dados: ClienteAtualizacaoRequest,
  ): Observable<ClienteResponse> {
    return this.http.put<ClienteResponse>(
      `${this.apiUrl}/${idCliente}`,
      dados,
    );
  }

  excluir(idCliente: number): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/${idCliente}`,
    );
  }
}