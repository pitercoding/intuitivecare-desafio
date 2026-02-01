import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Operadora } from '../models/operadora.model';
import { DespesaConsolidada } from '../models/despesa.model';

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  first: boolean;
  last: boolean;
}

export interface Estatisticas {
  total: number;
  media: number;
  top5: string[];
  totalPorUF?: { [uf: string]: number };
}

@Injectable({ providedIn: 'root' })
export class OperadoraService {
  private apiUrl = 'http://localhost:8080/api/operadoras';

  constructor(private http: HttpClient) {}

   // Lista paginada de operadoras
  listar(page = 0, size = 10): Observable<Page<Operadora>> {
    return this.http.get<Page<Operadora>>(`${this.apiUrl}?page=${page}&size=${size}`);
  }

  // Busca operadora pelo CNPJ
  buscarPorCnpj(cnpj: string): Observable<Operadora> {
    return this.http.get<Operadora>(`${this.apiUrl}/${cnpj}`);
  }

  // Busca despesas de uma operadora pelo CNPJ
  getDespesas(cnpj: string): Observable<DespesaConsolidada[]> {
    return this.http.get<DespesaConsolidada[]>(`${this.apiUrl}/${cnpj}/despesas`);
  }

   // Busca estatísticas agregadas
  getEstatisticas(): Observable<Estatisticas> {
    return this.http.get<Estatisticas>(`${this.apiUrl}/estatisticas`);
  }
}
