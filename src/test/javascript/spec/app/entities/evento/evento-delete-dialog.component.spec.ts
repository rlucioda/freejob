import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FreejobTestModule } from '../../../test.module';
import { EventoDeleteDialogComponent } from 'app/entities/evento/evento-delete-dialog.component';
import { EventoService } from 'app/entities/evento/evento.service';

describe('Component Tests', () => {
  describe('Evento Management Delete Component', () => {
    let comp: EventoDeleteDialogComponent;
    let fixture: ComponentFixture<EventoDeleteDialogComponent>;
    let service: EventoService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FreejobTestModule],
        declarations: [EventoDeleteDialogComponent]
      })
        .overrideTemplate(EventoDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EventoDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EventoService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
