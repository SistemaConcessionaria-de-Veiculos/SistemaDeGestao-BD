export type TipoCliente = 'FISICA' | 'JURIDICA';

export interface ClienteCadastroRequest {
  nome: string;
  email: string;
  telefone: string;
  rua: string;
  numero: string;
  cep: string;
  tipo: TipoCliente;
  cpf: string | null;
  cnpj: string | null;
}

export interface ClienteAtualizacaoRequest {
  nome: string;
  email: string;
  telefone: string;
  rua: string;
  numero: string;
  cep: string;
  tipo: TipoCliente;
  cpf: string | null;
  cnpj: string | null;
}

export interface ClienteResponse {
  idCliente: number;
  nome: string;
  email: string;
  telefone: string;
  rua: string;
  numero: string;
  cep: string;
  tipo: TipoCliente;
  cpf: string | null;
  cnpj: string | null;
}