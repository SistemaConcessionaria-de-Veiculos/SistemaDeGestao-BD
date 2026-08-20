import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import {
  VeiculoAtualizacaoRequest,
  VeiculoCadastroRequest,
  VeiculoListagemResponse,
  VeiculoResponse,
} from '../models/veiculo';

@Injectable({
  providedIn: 'root',
})
export class VeiculoService {
  private readonly apiUrl =
    'http://localhost:8080/api/veiculos';

  constructor(
    private readonly http: HttpClient,
  ) {}

  listar(): Observable<VeiculoListagemResponse[]> {
    return this.http.get<VeiculoListagemResponse[]>(
      this.apiUrl,
    );
  }

  buscar(chassi: string): Observable<VeiculoResponse> {
    return this.http.get<VeiculoResponse>(
      `${this.apiUrl}/${encodeURIComponent(chassi)}`,
    );
  }

  cadastrar(
    dados: VeiculoCadastroRequest,
  ): Observable<VeiculoResponse> {
    return this.http.post<VeiculoResponse>(
      this.apiUrl,
      dados,
    );
  }

  atualizar(
    chassi: string,
    dados: VeiculoAtualizacaoRequest,
  ): Observable<VeiculoResponse> {
    return this.http.put<VeiculoResponse>(
      `${this.apiUrl}/${encodeURIComponent(chassi)}`,
      dados,
    );
  }

  excluir(chassi: string): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/${encodeURIComponent(chassi)}`,
    );
  }
}