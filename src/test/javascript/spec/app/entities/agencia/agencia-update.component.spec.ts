import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { FreejobTestModule } from '../../../test.module';
import { AgenciaUpdateComponent } from 'app/entities/agencia/agencia-update.component';
import { AgenciaService } from 'app/entities/agencia/agencia.service';
import { Agencia } from 'app/shared/model/agencia.model';

describe('Component Tests', () => {
  describe('Agencia Management Update Component', () => {
    let comp: AgenciaUpdateComponent;
    let fixture: ComponentFixture<AgenciaUpdateComponent>;
    let service: AgenciaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FreejobTestModule],
        declarations: [AgenciaUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(AgenciaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AgenciaUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AgenciaService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Agencia(123);
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
        const entity = new Agencia();
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
