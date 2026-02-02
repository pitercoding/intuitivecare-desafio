import { Routes } from '@angular/router';
import { OperadorasListComponent } from './pages/operadoras-list/operadoras-list.component';
import { OperadoraDetalheComponent } from './pages/operadora-detalhe/operadora-detalhe.component';
import { EstatisticasComponent } from './pages/estatisticas/estatisticas.component';

export const routes: Routes = [
  { path: '', redirectTo: '/operadoras', pathMatch: 'full' },
  { path: 'operadoras', component: OperadorasListComponent },
  { path: 'operadoras/estatisticas', component: EstatisticasComponent },
  { path: 'operadoras/:registroAns', component: OperadoraDetalheComponent },
  { path: '**', redirectTo: '/operadoras' }
];
