import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Endereco } from 'app/shared/model/endereco.model';
import { EnderecoService } from './endereco.service';
import { EnderecoComponent } from './endereco.component';
import { EnderecoDetailComponent } from './endereco-detail.component';
import { EnderecoUpdateComponent } from './endereco-update.component';
import { EnderecoDeletePopupComponent } from './endereco-delete-dialog.component';
import { IEndereco } from 'app/shared/model/endereco.model';

@Injectable({ providedIn: 'root' })
export class EnderecoResolve implements Resolve<IEndereco> {
  constructor(private service: EnderecoService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEndereco> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((endereco: HttpResponse<Endereco>) => endereco.body));
    }
    return of(new Endereco());
  }
}

export const enderecoRoute: Routes = [
  {
    path: '',
    component: EnderecoComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'freejobApp.endereco.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: EnderecoDetailComponent,
    resolve: {
      endereco: EnderecoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'freejobApp.endereco.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: EnderecoUpdateComponent,
    resolve: {
      endereco: EnderecoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'freejobApp.endereco.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: EnderecoUpdateComponent,
    resolve: {
      endereco: EnderecoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'freejobApp.endereco.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const enderecoPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: EnderecoDeletePopupComponent,
    resolve: {
      endereco: EnderecoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'freejobApp.endereco.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
