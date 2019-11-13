import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FreejobTestModule } from '../../../test.module';
import { EventoDetailComponent } from 'app/entities/evento/evento-detail.component';
import { Evento } from 'app/shared/model/evento.model';

describe('Component Tests', () => {
  describe('Evento Management Detail Component', () => {
    let comp: EventoDetailComponent;
    let fixture: ComponentFixture<EventoDetailComponent>;
    const route = ({ data: of({ evento: new Evento(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FreejobTestModule],
        declarations: [EventoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(EventoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EventoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.evento).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
