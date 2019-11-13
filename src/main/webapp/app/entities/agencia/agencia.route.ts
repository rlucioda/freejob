import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Agencia } from 'app/shared/model/agencia.model';
import { AgenciaService } from './agencia.service';
import { AgenciaComponent } from './agencia.component';
import { AgenciaDetailComponent } from './agencia-detail.component';
import { AgenciaUpdateComponent } from './agencia-update.component';
import { AgenciaDeletePopupComponent } from './agencia-delete-dialog.component';
import { IAgencia } from 'app/shared/model/agencia.model';

@Injectable({ providedIn: 'root' })
export class AgenciaResolve implements Resolve<IAgencia> {
  constructor(private service: AgenciaService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAgencia> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((agencia: HttpResponse<Agencia>) => agencia.body));
    }
    return of(new Agencia());
  }
}

export const agenciaRoute: Routes = [
  {
    path: '',
    component: AgenciaComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'freejobApp.agencia.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: AgenciaDetailComponent,
    resolve: {
      agencia: AgenciaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'freejobApp.agencia.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: AgenciaUpdateComponent,
    resolve: {
      agencia: AgenciaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'freejobApp.agencia.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: AgenciaUpdateComponent,
    resolve: {
      agencia: AgenciaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'freejobApp.agencia.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const agenciaPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: AgenciaDeletePopupComponent,
    resolve: {
      agencia: AgenciaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'freejobApp.agencia.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
