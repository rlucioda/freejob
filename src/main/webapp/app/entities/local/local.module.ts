import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FreejobSharedModule } from 'app/shared/shared.module';
import { LocalComponent } from './local.component';
import { LocalDetailComponent } from './local-detail.component';
import { LocalUpdateComponent } from './local-update.component';
import { LocalDeletePopupComponent, LocalDeleteDialogComponent } from './local-delete-dialog.component';
import { localRoute, localPopupRoute } from './local.route';

const ENTITY_STATES = [...localRoute, ...localPopupRoute];

@NgModule({
  imports: [FreejobSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [LocalComponent, LocalDetailComponent, LocalUpdateComponent, LocalDeleteDialogComponent, LocalDeletePopupComponent],
  entryComponents: [LocalDeleteDialogComponent]
})
export class FreejobLocalModule {}
