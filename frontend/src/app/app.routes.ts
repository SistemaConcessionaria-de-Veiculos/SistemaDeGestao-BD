import { Routes } from '@angular/router';

import { Dashboard } from './pages/dashboard/dashboard';
import { ClienteCadastro } from './pages/cliente-cadastro/cliente-cadastro';
import { ClienteListagem } from './pages/cliente-listagem/cliente-listagem';
import { Relatorios } from './pages/relatorios/relatorios';
import { VendedorCadastro } from './pages/vendedor-cadastro/vendedor-cadastro';
import { VendedorListagem } from './pages/vendedor-listagem/vendedor-listagem';
import { VeiculoCadastro } from './pages/veiculo-cadastro/veiculo-cadastro';
import { VeiculoListagem } from './pages/veiculo-listagem/veiculo-listagem';
import { VendaCadastro } from './pages/venda-cadastro/venda-cadastro';
import { VendaListagem } from './pages/venda-listagem/venda-listagem';

export const routes: Routes = [
  {
    path: 'dashboard',
    component: Dashboard,
  },

  {
    path: 'clientes/cadastro',
    component: ClienteCadastro,
  },

  {
    path: 'clientes',
    component: ClienteListagem,
  },

  {
    path: 'vendedores/cadastro',
    component: VendedorCadastro,
  },

  {
    path: 'vendedores',
    component: VendedorListagem,
  },

  {
    path: 'veiculos/cadastro',
    component: VeiculoCadastro,
  },

  {
    path: 'veiculos',
    component: VeiculoListagem,
  },

  {
    path: 'vendas/cadastro',
    component: VendaCadastro,
  },

  {
    path: 'vendas',
    component: VendaListagem,
  },

  {
    path: 'relatorios',
    component: Relatorios,
  },

  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'dashboard',
  },

  {
    path: '**',
    redirectTo: 'dashboard',
  },
];