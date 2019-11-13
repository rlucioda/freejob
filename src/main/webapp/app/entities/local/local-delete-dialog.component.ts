import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILocal } from 'app/shared/model/local.model';
import { LocalService } from './local.service';

@Component({
  selector: 'jhi-local-delete-dialog',
  templateUrl: './local-delete-dialog.component.html'
})
export class LocalDeleteDialogComponent {
  local: ILocal;

  constructor(protected localService: LocalService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.localService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'localListModification',
        content: 'Deleted an local'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-local-delete-popup',
  template: ''
})
export class LocalDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ local }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(LocalDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.local = local;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/local', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/local', { outlets: { popup: null } }]);
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
