import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEvento } from 'app/shared/model/evento.model';
import { EventoService } from './evento.service';

@Component({
  selector: 'jhi-evento-delete-dialog',
  templateUrl: './evento-delete-dialog.component.html'
})
export class EventoDeleteDialogComponent {
  evento: IEvento;

  constructor(protected eventoService: EventoService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.eventoService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'eventoListModification',
        content: 'Deleted an evento'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-evento-delete-popup',
  template: ''
})
export class EventoDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ evento }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(EventoDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.evento = evento;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/evento', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/evento', { outlets: { popup: null } }]);
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
