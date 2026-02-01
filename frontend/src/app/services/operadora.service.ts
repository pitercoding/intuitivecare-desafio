import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Operadora } from '../models/operadora.model';

@Injectable({
  providedIn: 'root',
})
export class OperadoraService {
  private readonly apiUrl = 'http://localhost:8080/api/operadoras';

  constructor(private http: HttpClient) {}

  listar(page = 0, limit = 10): Observable<any> {
    const params = new HttpParams().set('page', page).set('limit', limit);

    return this.http.get<any>(this.apiUrl, { params });
  }

  buscarPorCnpj(cnpj: string): Observable<Operadora> {
    return this.http.get<Operadora>(`${this.apiUrl}/${cnpj}`);
  }
}
