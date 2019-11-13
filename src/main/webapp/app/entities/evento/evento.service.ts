import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IEvento } from 'app/shared/model/evento.model';

type EntityResponseType = HttpResponse<IEvento>;
type EntityArrayResponseType = HttpResponse<IEvento[]>;

@Injectable({ providedIn: 'root' })
export class EventoService {
  public resourceUrl = SERVER_API_URL + 'api/eventos';

  constructor(protected http: HttpClient) {}

  create(evento: IEvento): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(evento);
    return this.http
      .post<IEvento>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(evento: IEvento): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(evento);
    return this.http
      .put<IEvento>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEvento>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEvento[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(evento: IEvento): IEvento {
    const copy: IEvento = Object.assign({}, evento, {
      dataInicio: evento.dataInicio != null && evento.dataInicio.isValid() ? evento.dataInicio.toJSON() : null,
      dataFim: evento.dataFim != null && evento.dataFim.isValid() ? evento.dataFim.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dataInicio = res.body.dataInicio != null ? moment(res.body.dataInicio) : null;
      res.body.dataFim = res.body.dataFim != null ? moment(res.body.dataFim) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((evento: IEvento) => {
        evento.dataInicio = evento.dataInicio != null ? moment(evento.dataInicio) : null;
        evento.dataFim = evento.dataFim != null ? moment(evento.dataFim) : null;
      });
    }
    return res;
  }
}
