import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FreejobTestModule } from '../../../test.module';
import { AgenciaDetailComponent } from 'app/entities/agencia/agencia-detail.component';
import { Agencia } from 'app/shared/model/agencia.model';

describe('Component Tests', () => {
  describe('Agencia Management Detail Component', () => {
    let comp: AgenciaDetailComponent;
    let fixture: ComponentFixture<AgenciaDetailComponent>;
    const route = ({ data: of({ agencia: new Agencia(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FreejobTestModule],
        declarations: [AgenciaDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(AgenciaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AgenciaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.agencia).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
