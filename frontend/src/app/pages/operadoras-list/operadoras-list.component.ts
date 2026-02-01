import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OperadoraService, Page } from '../../services/operadora.service';
import { Operadora } from '../../models/operadora.model';

@Component({
  selector: 'app-operadoras-list.component',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './operadoras-list.component.html',
  styleUrl: './operadoras-list.component.scss',
})
export class OperadorasListComponent implements OnInit {
  operadoras: Operadora[] = [];

  page = 0;
  size = 10;
  totalPages = 0;
  loading = false;

  constructor(private operadoraService: OperadoraService) {}

  ngOnInit(): void {
    this.carregarOperadoras();
  }

  carregarOperadoras(): void {
    this.loading = true;

    this.operadoraService.listar(this.page, this.size).subscribe({
      next: (res: Page<Operadora>) => {
        this.operadoras = res.content;
        this.totalPages = res.totalPages;
        this.page = res.number;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      },
    });
  }

  proximaPagina(): void {
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.carregarOperadoras();
    }
  }

  paginaAnterior(): void {
    if (this.page > 0) {
      this.page--;
      this.carregarOperadoras();
    }
  }
}
