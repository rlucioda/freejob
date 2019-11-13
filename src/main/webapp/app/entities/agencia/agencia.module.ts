import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FreejobSharedModule } from 'app/shared/shared.module';
import { AgenciaComponent } from './agencia.component';
import { AgenciaDetailComponent } from './agencia-detail.component';
import { AgenciaUpdateComponent } from './agencia-update.component';
import { AgenciaDeletePopupComponent, AgenciaDeleteDialogComponent } from './agencia-delete-dialog.component';
import { agenciaRoute, agenciaPopupRoute } from './agencia.route';

const ENTITY_STATES = [...agenciaRoute, ...agenciaPopupRoute];

@NgModule({
  imports: [FreejobSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    AgenciaComponent,
    AgenciaDetailComponent,
    AgenciaUpdateComponent,
    AgenciaDeleteDialogComponent,
    AgenciaDeletePopupComponent
  ],
  entryComponents: [AgenciaDeleteDialogComponent]
})
export class FreejobAgenciaModule {}
