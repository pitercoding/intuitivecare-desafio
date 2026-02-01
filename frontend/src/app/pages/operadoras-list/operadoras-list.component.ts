import { Component, OnInit } from '@angular/core';
import { OperadoraService } from '../../services/operadora.service';
import { Operadora } from '../../models/operadora.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-operadoras-list.component',
  imports: [CommonModule],
  templateUrl: './operadoras-list.component.html',
  styleUrl: './operadoras-list.component.scss',
})
export class OperadorasListComponent implements OnInit {

  operadoras: Operadora[] = [];

  page = 0;
  size = 10;

  constructor(private OperadoraService: OperadoraService) {}
  ngOnInit(): void {
    this.carregarOperadoras();
  }

  carregarOperadoras(): void {
    this.OperadoraService.listar(this.page, this.size)
      .subscribe(response => {
        this.operadoras = response.data; // Page do Spring
      });
  }

}
