import { Component, OnInit, ViewChild, ElementRef, AfterViewInit, OnDestroy } from '@angular/core';
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
  // O setter será chamado quando o elemento #graficoUF for renderizado pelo *ngIf
  @ViewChild('graficoUF') set graficoUF(el: ElementRef<HTMLCanvasElement> | undefined) {
    if (el) {
      // Após o elemento canvas exixir, o gráfico é criado
      this._graficoUF = el;
      this.criarGrafico();
    }
  }

  private _graficoUF?: ElementRef<HTMLCanvasElement>;
  private chart?: Chart<'bar', number[], string>;

  loading = true;
  estatisticas?: Estatisticas;

  constructor(private operadoraService: OperadoraService) {}

  ngOnInit(): void {
    this.operadoraService.getEstatisticas().subscribe({
      next: (res: Estatisticas) => {
        this.estatisticas = res;
        // Apenas estado. O setter do ViewChild cuidará do resto.
        this.loading = false;
      },
      error: (err) => {
        console.error('Erro ao buscar estatísticas:', err);
        this.loading = false;
      },
    });
  }

  ngOnDestroy(): void {
    this.chart?.destroy();
  }

  private criarGrafico(): void {
    // Garante os dados e o elemento canvas antes de prosseguir
    if (!this.estatisticas?.totalPorUF || !this._graficoUF?.nativeElement || !this.hasUFData) {
      return;
    }

    // Destrói um gráfico anterior se ele existir, para evitar memory leaks
    if (this.chart) {
      this.chart.destroy();
    }

    const labels = Object.keys(this.estatisticas.totalPorUF);
    const rawData = Object.values(this.estatisticas.totalPorUF) as number[];
    const data = rawData.map(v => v / 1_000_000_000); // Converte para bilhões

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
