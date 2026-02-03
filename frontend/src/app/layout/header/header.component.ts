import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
})
export class HeaderComponent {
  constructor(private router: Router) {}

  isOperadorasActive(): boolean {
    const url = this.router.url;
    return url.startsWith('/operadoras') && !url.startsWith('/operadoras/estatisticas');
  }
}
