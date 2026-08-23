export interface ResumoVendasResponse {
  numeroNota: number;
  dataDaVenda: string;
  idCliente: number;
  nomeCliente: string;
  matriculaVendedor: number;
  nomeVendedor: string;
  quantidadeVeiculos: number;
  valorTotalVenda: number;
}

export interface ClientesComprasResponse {
  idCliente: number;
  nomeCliente: string;
  quantidadeCompras: number;
  quantidadeVeiculos: number;
  valorTotalCompras: number;
}

export interface VeiculosCustomizacoesResponse {
  chassi: string;
  marca: string;
  modelo: string;
  statusDisponibilidade: string;
  tipoVeiculo: string;
  placa: string | null;
  quilometragem: number | null;
  valorVeiculo: number;
  quantidadeCustomizacoes: number;
  customizacoes: string;
  custoCustomizacoes: number;
  valorTotalComCustomizacoes: number;
}