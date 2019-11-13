import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FreejobTestModule } from '../../../test.module';
import { LocalDetailComponent } from 'app/entities/local/local-detail.component';
import { Local } from 'app/shared/model/local.model';

describe('Component Tests', () => {
  describe('Local Management Detail Component', () => {
    let comp: LocalDetailComponent;
    let fixture: ComponentFixture<LocalDetailComponent>;
    const route = ({ data: of({ local: new Local(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FreejobTestModule],
        declarations: [LocalDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(LocalDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LocalDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.local).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
