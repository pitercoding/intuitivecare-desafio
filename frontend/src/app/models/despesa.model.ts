export interface DespesaConsolidada {
  npj: string;
  razaoSocial: string;
  ano: number;
  trimestre: number;
  valor: number;
  uf?: string;
  nomeFantasia?: string;
  modalidade?: string;
}
