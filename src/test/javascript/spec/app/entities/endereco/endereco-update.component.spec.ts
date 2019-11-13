import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { FreejobTestModule } from '../../../test.module';
import { EnderecoUpdateComponent } from 'app/entities/endereco/endereco-update.component';
import { EnderecoService } from 'app/entities/endereco/endereco.service';
import { Endereco } from 'app/shared/model/endereco.model';

describe('Component Tests', () => {
  describe('Endereco Management Update Component', () => {
    let comp: EnderecoUpdateComponent;
    let fixture: ComponentFixture<EnderecoUpdateComponent>;
    let service: EnderecoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FreejobTestModule],
        declarations: [EnderecoUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(EnderecoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EnderecoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EnderecoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Endereco(123);
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
        const entity = new Endereco();
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
