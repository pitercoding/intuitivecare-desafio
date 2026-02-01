import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Operadora } from '../models/operadora.model';

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  first: boolean;
  last: boolean;
}

@Injectable({ providedIn: 'root' })
export class OperadoraService {
  private apiUrl = 'http://localhost:8080/api/operadoras';

  constructor(private http: HttpClient) {}

  listar(page = 0, size = 10): Observable<Page<Operadora>> {
    return this.http.get<Page<Operadora>>(`${this.apiUrl}?page=${page}&size=${size}`);
  }

  buscarPorCnpj(cnpj: string): Observable<Operadora> {
    return this.http.get<Operadora>(`${this.apiUrl}/${cnpj}`);
  }
}
