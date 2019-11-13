import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ILocal } from 'app/shared/model/local.model';

type EntityResponseType = HttpResponse<ILocal>;
type EntityArrayResponseType = HttpResponse<ILocal[]>;

@Injectable({ providedIn: 'root' })
export class LocalService {
  public resourceUrl = SERVER_API_URL + 'api/locals';

  constructor(protected http: HttpClient) {}

  create(local: ILocal): Observable<EntityResponseType> {
    return this.http.post<ILocal>(this.resourceUrl, local, { observe: 'response' });
  }

  update(local: ILocal): Observable<EntityResponseType> {
    return this.http.put<ILocal>(this.resourceUrl, local, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILocal>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILocal[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
