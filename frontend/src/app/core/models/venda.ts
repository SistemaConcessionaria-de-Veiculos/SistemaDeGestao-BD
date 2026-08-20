export interface VendaCadastroRequest {
  idCliente: number;
  matriculaVendedor: number;
  valorTotalVenda: number;
  dataDaVenda: string;
  chassis?: string[];
}

export interface VendaAtualizacaoRequest {
  idCliente: number;
  matriculaVendedor: number;
  valorTotalVenda: number;
  dataDaVenda: string;
  chassis?: string[];
}

export interface VendaResponse {
  numeroNota: number;
  idCliente: number;
  matriculaVendedor: number;
  valorTotalVenda: number;
  dataDaVenda: string;
  chassis: string[];
}