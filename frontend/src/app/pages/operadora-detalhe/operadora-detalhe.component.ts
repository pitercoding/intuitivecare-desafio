import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { OperadoraService } from '../../services/operadora.service';
import { Operadora } from '../../models/operadora.model';
import { DespesaConsolidada } from '../../models/despesa.model';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-operadora-detalhe.component',
  standalone: true,
  imports: [CommonModule, CurrencyPipe, RouterModule],
  templateUrl: './operadora-detalhe.component.html',
  styleUrl: './operadora-detalhe.component.scss',
})
export class OperadoraDetalheComponent implements OnInit {
  operadora?: Operadora;
  despesas: DespesaConsolidada[] = [];
  despesasPagina: DespesaConsolidada[] = [];
  loading = false;

  page = 0;
  size = 10;
  totalPages = 0;

  constructor(
    private route: ActivatedRoute,
    private operadoraService: OperadoraService,
  ) {}

  ngOnInit(): void {
    const registroAns = this.route.snapshot.paramMap.get('registroAns');
    if (!registroAns) return;

    this.loading = true;

    this.operadoraService.buscarPorRegistroAns(registroAns).subscribe({
      next: (op) => (this.operadora = op),
      error: () => (this.loading = false),
    });

    this.operadoraService.getDespesasPorRegistroAns(registroAns).subscribe({
      next: (d) => {
        this.despesas = d;
        this.totalPages = Math.ceil(this.despesas.length / this.size);
        this.carregarPagina();
      },
      error: () => (this.loading = false),
      complete: () => (this.loading = false),
    });
  }

  carregarPagina(): void {
    const start = this.page * this.size;
    const end = start + this.size;
    this.despesasPagina = this.despesas.slice(start, end);
  }

  proximaPagina(): void {
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.carregarPagina();
    }
  }

  paginaAnterior(): void {
    if (this.page > 0) {
      this.page--;
      this.carregarPagina();
    }
  }
}
