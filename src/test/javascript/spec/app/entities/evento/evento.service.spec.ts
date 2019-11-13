import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { EventoService } from 'app/entities/evento/evento.service';
import { IEvento, Evento } from 'app/shared/model/evento.model';
import { TipoEvento } from 'app/shared/model/enumerations/tipo-evento.model';

describe('Service Tests', () => {
  describe('Evento Service', () => {
    let injector: TestBed;
    let service: EventoService;
    let httpMock: HttpTestingController;
    let elemDefault: IEvento;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(EventoService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Evento(0, 'AAAAAAA', 'AAAAAAA', currentDate, currentDate, false, TipoEvento.PANFLETAGEM);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dataInicio: currentDate.format(DATE_TIME_FORMAT),
            dataFim: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a Evento', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dataInicio: currentDate.format(DATE_TIME_FORMAT),
            dataFim: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dataInicio: currentDate,
            dataFim: currentDate
          },
          returnedFromService
        );
        service
          .create(new Evento(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a Evento', () => {
        const returnedFromService = Object.assign(
          {
            nomeEvento: 'BBBBBB',
            descricaEvento: 'BBBBBB',
            dataInicio: currentDate.format(DATE_TIME_FORMAT),
            dataFim: currentDate.format(DATE_TIME_FORMAT),
            statusJob: true,
            tipoEvento: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataInicio: currentDate,
            dataFim: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of Evento', () => {
        const returnedFromService = Object.assign(
          {
            nomeEvento: 'BBBBBB',
            descricaEvento: 'BBBBBB',
            dataInicio: currentDate.format(DATE_TIME_FORMAT),
            dataFim: currentDate.format(DATE_TIME_FORMAT),
            statusJob: true,
            tipoEvento: 'BBBBBB'
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dataInicio: currentDate,
            dataFim: currentDate
          },
          returnedFromService
        );
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Evento', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
