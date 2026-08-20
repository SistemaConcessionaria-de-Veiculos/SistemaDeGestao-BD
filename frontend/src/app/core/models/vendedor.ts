export interface VendedorCadastroRequest {
  nome: string;
  cpf: string;
}

export interface VendedorAtualizacaoRequest {
  nome: string;
  cpf: string;
}

export interface VendedorResponse {
  matricula: number;
  nome: string;
  cpf: string;
}