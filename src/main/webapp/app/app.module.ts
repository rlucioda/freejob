import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { FreejobSharedModule } from 'app/shared/shared.module';
import { FreejobCoreModule } from 'app/core/core.module';
import { FreejobAppRoutingModule } from './app-routing.module';
import { FreejobHomeModule } from './home/home.module';
import { FreejobEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { JhiMainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    FreejobSharedModule,
    FreejobCoreModule,
    FreejobHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    FreejobEntityModule,
    FreejobAppRoutingModule
  ],
  declarations: [JhiMainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
  bootstrap: [JhiMainComponent]
})
export class FreejobAppModule {}
