import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { FreejobTestModule } from '../../../test.module';
import { EventoUpdateComponent } from 'app/entities/evento/evento-update.component';
import { EventoService } from 'app/entities/evento/evento.service';
import { Evento } from 'app/shared/model/evento.model';

describe('Component Tests', () => {
  describe('Evento Management Update Component', () => {
    let comp: EventoUpdateComponent;
    let fixture: ComponentFixture<EventoUpdateComponent>;
    let service: EventoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FreejobTestModule],
        declarations: [EventoUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(EventoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EventoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EventoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Evento(123);
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
        const entity = new Evento();
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
