import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'job',
        loadChildren: () => import('./job/job.module').then(m => m.FreejobJobModule)
      },
      {
        path: 'agencia',
        loadChildren: () => import('./agencia/agencia.module').then(m => m.FreejobAgenciaModule)
      },
      {
        path: 'evento',
        loadChildren: () => import('./evento/evento.module').then(m => m.FreejobEventoModule)
      },
      {
        path: 'local',
        loadChildren: () => import('./local/local.module').then(m => m.FreejobLocalModule)
      },
      {
        path: 'endereco',
        loadChildren: () => import('./endereco/endereco.module').then(m => m.FreejobEnderecoModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class FreejobEntityModule {}
