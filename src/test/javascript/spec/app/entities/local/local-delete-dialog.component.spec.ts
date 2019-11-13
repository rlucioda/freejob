import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FreejobTestModule } from '../../../test.module';
import { LocalDeleteDialogComponent } from 'app/entities/local/local-delete-dialog.component';
import { LocalService } from 'app/entities/local/local.service';

describe('Component Tests', () => {
  describe('Local Management Delete Component', () => {
    let comp: LocalDeleteDialogComponent;
    let fixture: ComponentFixture<LocalDeleteDialogComponent>;
    let service: LocalService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FreejobTestModule],
        declarations: [LocalDeleteDialogComponent]
      })
        .overrideTemplate(LocalDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LocalDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LocalService);
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
