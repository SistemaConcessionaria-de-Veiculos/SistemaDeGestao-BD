export type StatusVeiculo =
  | 'disponivel'
  | 'reservado'
  | 'vendido';

export type TipoVeiculo =
  | 'NOVO'
  | 'USADO';

export interface VeiculoCadastroRequest {
  chassi: string;
  numeroNota: number | null;
  marca: string;
  modelo: string;
  cor: string;
  dataFabricacao: string;
  statusDisponibilidade: StatusVeiculo;
  valorVeiculo: number;
  tipo: TipoVeiculo;
  placa: string | null;
  quilometragem: number | null;
}

export interface VeiculoAtualizacaoRequest {
  numeroNota: number | null;
  marca: string;
  modelo: string;
  cor: string;
  dataFabricacao: string;
  statusDisponibilidade: StatusVeiculo;
  valorVeiculo: number;
  tipo: TipoVeiculo;
  placa: string | null;
  quilometragem: number | null;
}

export interface VeiculoResponse {
  chassi: string;
  numeroNota: number | null;
  marca: string;
  modelo: string;
  cor: string;
  dataFabricacao: string;
  statusDisponibilidade: StatusVeiculo;
  valorVeiculo: number;
  tipo: TipoVeiculo;
  placa: string | null;
  quilometragem: number | null;
}

export interface VeiculoListagemResponse {
  chassi: string;
  marca: string;
  modelo: string;
  cor: string;
  dataFabricacao: string;
  valorVeiculo: number;
  statusDisponibilidade: StatusVeiculo;
  tipo: TipoVeiculo;
  placa: string | null;
  quilometragem: number | null;
}