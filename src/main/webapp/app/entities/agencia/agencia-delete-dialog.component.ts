import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAgencia } from 'app/shared/model/agencia.model';
import { AgenciaService } from './agencia.service';

@Component({
  selector: 'jhi-agencia-delete-dialog',
  templateUrl: './agencia-delete-dialog.component.html'
})
export class AgenciaDeleteDialogComponent {
  agencia: IAgencia;

  constructor(protected agenciaService: AgenciaService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.agenciaService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'agenciaListModification',
        content: 'Deleted an agencia'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-agencia-delete-popup',
  template: ''
})
export class AgenciaDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ agencia }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(AgenciaDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.agencia = agencia;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/agencia', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/agencia', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
