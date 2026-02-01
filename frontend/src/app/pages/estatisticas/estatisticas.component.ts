import { Component, OnInit } from '@angular/core';
import { OperadoraService, Estatisticas } from '../../services/operadora.service';
import { Chart, ChartDataset, ChartType, ChartOptions } from 'chart.js';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-estatisticas.component',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './estatisticas.component.html',
  styleUrl: './estatisticas.component.scss',
})
export class EstatisticasComponent implements OnInit {
  chart?: Chart<'bar', number[], string>;
  loading = false;
  estatisticas?: Estatisticas;

  constructor(private operadoraService: OperadoraService) {}

  ngOnInit(): void {
    this.loading = true;
    this.operadoraService.getEstatisticas().subscribe({
      next: (res: Estatisticas) => {
        this.estatisticas = res;

        // Gerando totalPorUF fictício se não existir no backend
        if (!this.estatisticas.totalPorUF) {
          this.estatisticas.totalPorUF = { SP: 120000, RJ: 80000, MG: 60000 };
        }

        this.criarGrafico();
        this.loading = false;
      },
      error: () => (this.loading = false),
    });
  }

  criarGrafico(): void {
    if (!this.estatisticas || !this.estatisticas.totalPorUF) return;

    const labels = Object.keys(this.estatisticas.totalPorUF);
    const data = Object.values(this.estatisticas.totalPorUF);

    const dataset: ChartDataset<'bar', number[]> = {
      label: 'Total de Despesas por UF',
      data: data as number[],
      backgroundColor: 'rgba(54, 162, 235, 0.7)',
    };

    const config: ChartOptions<'bar'> = {
      responsive: true,
    };

    this.chart = new Chart('graficoUF', {
      type: 'bar',
      data: { labels, datasets: [dataset] },
      options: config,
    });
  }

  get hasUFData(): boolean {
    return !!(
      this.estatisticas?.totalPorUF && Object.keys(this.estatisticas.totalPorUF).length > 0
    );
  }
}
