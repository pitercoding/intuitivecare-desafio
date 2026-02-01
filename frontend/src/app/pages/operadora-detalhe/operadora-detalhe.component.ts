import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { OperadoraService } from '../../services/operadora.service';
import { Operadora } from '../../models/operadora.model';
import { DespesaConsolidada } from '../../models/despesa.model';
import { CommonModule, CurrencyPipe } from '@angular/common';

@Component({
  selector: 'app-operadora-detalhe.component',
  standalone: true,
  imports: [
    CurrencyPipe,
    CommonModule
  ],
  templateUrl: './operadora-detalhe.component.html',
  styleUrl: './operadora-detalhe.component.scss',
})
export class OperadoraDetalheComponent implements OnInit {
  operadora?: Operadora;
  despesas: DespesaConsolidada[] = [];
  loading = false;

  constructor(
    private route: ActivatedRoute,
    private operadoraService: OperadoraService
  ) {}

  ngOnInit(): void {
    const cnpj = this.route.snapshot.paramMap.get('cnpj');
    if (!cnpj) return;

    this.loading = true;

    this.operadoraService.buscarPorCnpj(cnpj).subscribe({
      next: op => this.operadora = op,
      error: () => this.loading = false
    });

    this.operadoraService.getDespesas(cnpj).subscribe({
      next: d => this.despesas = d,
      error: () => this.loading = false,
      complete: () => this.loading = false
    });
  }
}
