import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { FreejobTestModule } from '../../../test.module';
import { LocalUpdateComponent } from 'app/entities/local/local-update.component';
import { LocalService } from 'app/entities/local/local.service';
import { Local } from 'app/shared/model/local.model';

describe('Component Tests', () => {
  describe('Local Management Update Component', () => {
    let comp: LocalUpdateComponent;
    let fixture: ComponentFixture<LocalUpdateComponent>;
    let service: LocalService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FreejobTestModule],
        declarations: [LocalUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(LocalUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LocalUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LocalService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Local(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Local();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
