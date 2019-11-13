import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FreejobSharedModule } from 'app/shared/shared.module';
import { EnderecoComponent } from './endereco.component';
import { EnderecoDetailComponent } from './endereco-detail.component';
import { EnderecoUpdateComponent } from './endereco-update.component';
import { EnderecoDeletePopupComponent, EnderecoDeleteDialogComponent } from './endereco-delete-dialog.component';
import { enderecoRoute, enderecoPopupRoute } from './endereco.route';

const ENTITY_STATES = [...enderecoRoute, ...enderecoPopupRoute];

@NgModule({
  imports: [FreejobSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    EnderecoComponent,
    EnderecoDetailComponent,
    EnderecoUpdateComponent,
    EnderecoDeleteDialogComponent,
    EnderecoDeletePopupComponent
  ],
  entryComponents: [EnderecoDeleteDialogComponent]
})
export class FreejobEnderecoModule {}
