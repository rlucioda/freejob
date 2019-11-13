import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JobService } from 'app/entities/job/job.service';
import { IJob, Job } from 'app/shared/model/job.model';
import { TipoPagamento } from 'app/shared/model/enumerations/tipo-pagamento.model';

describe('Service Tests', () => {
  describe('Job Service', () => {
    let injector: TestBed;
    let service: JobService;
    let httpMock: HttpTestingController;
    let elemDefault: IJob;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(JobService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Job(0, 'AAAAAAA', 0, 0, 'AAAAAAA', currentDate, TipoPagamento.DINHEIRO, false);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dataPagamento: currentDate.format(DATE_TIME_FORMAT)
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

      it('should create a Job', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dataPagamento: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dataPagamento: currentDate
          },
          returnedFromService
        );
        service
          .create(new Job(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a Job', () => {
        const returnedFromService = Object.assign(
          {
            nomeJob: 'BBBBBB',
            valorHora: 1,
            tempoEvento: 1,
            observacao: 'BBBBBB',
            dataPagamento: currentDate.format(DATE_TIME_FORMAT),
            tipoPagamento: 'BBBBBB',
            statusPagamento: true
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataPagamento: currentDate
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

      it('should return a list of Job', () => {
        const returnedFromService = Object.assign(
          {
            nomeJob: 'BBBBBB',
            valorHora: 1,
            tempoEvento: 1,
            observacao: 'BBBBBB',
            dataPagamento: currentDate.format(DATE_TIME_FORMAT),
            tipoPagamento: 'BBBBBB',
            statusPagamento: true
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dataPagamento: currentDate
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

      it('should delete a Job', () => {
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
