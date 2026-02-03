import { Component, OnInit, ViewChild, ElementRef, OnDestroy } from '@angular/core';
import { OperadoraService, Estatisticas } from '../../services/operadora.service';
import { Chart, ChartDataset, ChartOptions, registerables } from 'chart.js';
import { CommonModule } from '@angular/common';

Chart.register(...registerables);

@Component({
  selector: 'app-estatisticas.component',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './estatisticas.component.html',
  styleUrls: ['./estatisticas.component.scss'],
})
export class EstatisticasComponent implements OnInit, OnDestroy {
  @ViewChild('graficoUF') set graficoUF(el: ElementRef<HTMLCanvasElement> | undefined) {
    if (el) {
      this._graficoUF = el;
      this.tentarCriarGrafico();
    }
  }

  private chart?: Chart<'bar', number[], string>;
  private dataReady = false;
  private _graficoUF?: ElementRef<HTMLCanvasElement>;

  loading = true;
  estatisticas?: Estatisticas;

  constructor(private operadoraService: OperadoraService) {}

  ngOnInit(): void {
    this.operadoraService.getEstatisticas().subscribe({
      next: (res: Estatisticas) => {
        this.estatisticas = res;
        this.dataReady = true;
        this.loading = false;
        this.tentarCriarGrafico();
      },
      error: (err) => {
        console.error('Erro ao buscar estatísticas:', err);
        this.loading = false;
      },
    });
  }

  ngOnDestroy(): void {
    this.chart?.destroy();
    this.chart = undefined;
  }

  private tentarCriarGrafico(): void {
    if (!this.dataReady || this.chart) {
      return;
    }

    // Garante os dados e o elemento canvas antes de prosseguir
    if (!this.estatisticas?.totalPorUF || !this._graficoUF?.nativeElement || !this.hasUFData) {
      return;
    }

    const labels = Object.keys(this.estatisticas.totalPorUF);
    const rawData = Object.values(this.estatisticas.totalPorUF) as number[];
    const data = rawData.map((v) => v / 1_000_000_000); // Converte para bilhoes

    const chartData: ChartDataset<'bar', number[]> = {
      label: 'Total de Despesas por UF (R$ bilhões)',
      data,
      backgroundColor: 'rgba(54, 162, 235, 0.5)',
      borderColor: 'rgba(54, 162, 235, 1)',
      borderWidth: 1,
    };

    const options: ChartOptions<'bar'> = {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        tooltip: {
          callbacks: {
            label: (ctx) => {
              const originalValue = rawData[ctx.dataIndex];
              return `R$ ${originalValue.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}`;
            },
          },
        },
      },
      scales: {
        y: {
          beginAtZero: true,
          title: { display: true, text: 'Valor (R$ bilhões)' },
          ticks: {
            callback: (value) => `${Number(value).toLocaleString('pt-BR')}`,
          },
        },
      },
    };

    this.chart = new Chart(this._graficoUF.nativeElement, {
      type: 'bar',
      data: { labels, datasets: [chartData] },
      options,
    });
  }

  get hasUFData(): boolean {
    return !!(
      this.estatisticas?.totalPorUF &&
      Object.keys(this.estatisticas.totalPorUF).length > 0
    );
  }
}
