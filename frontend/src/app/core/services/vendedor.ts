import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import {
  VendedorAtualizacaoRequest,
  VendedorCadastroRequest,
  VendedorResponse,
} from '../models/vendedor';

@Injectable({
  providedIn: 'root',
})
export class VendedorService {
  private readonly apiUrl =
    'http://localhost:8080/api/vendedores';

  constructor(
    private readonly http: HttpClient,
  ) {}

  listar(): Observable<VendedorResponse[]> {
    return this.http.get<VendedorResponse[]>(
      this.apiUrl,
    );
  }

  buscar(matricula: number): Observable<VendedorResponse> {
    return this.http.get<VendedorResponse>(
      `${this.apiUrl}/${matricula}`,
    );
  }

  cadastrar(
    dados: VendedorCadastroRequest,
  ): Observable<VendedorResponse> {
    return this.http.post<VendedorResponse>(
      this.apiUrl,
      dados,
    );
  }

  atualizar(
    matricula: number,
    dados: VendedorAtualizacaoRequest,
  ): Observable<VendedorResponse> {
    return this.http.put<VendedorResponse>(
      `${this.apiUrl}/${matricula}`,
      dados,
    );
  }

  excluir(matricula: number): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/${matricula}`,
    );
  }
}