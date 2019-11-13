import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FreejobTestModule } from '../../../test.module';
import { AgenciaDeleteDialogComponent } from 'app/entities/agencia/agencia-delete-dialog.component';
import { AgenciaService } from 'app/entities/agencia/agencia.service';

describe('Component Tests', () => {
  describe('Agencia Management Delete Component', () => {
    let comp: AgenciaDeleteDialogComponent;
    let fixture: ComponentFixture<AgenciaDeleteDialogComponent>;
    let service: AgenciaService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FreejobTestModule],
        declarations: [AgenciaDeleteDialogComponent]
      })
        .overrideTemplate(AgenciaDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AgenciaDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AgenciaService);
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
