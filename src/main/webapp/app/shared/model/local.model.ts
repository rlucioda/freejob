import { IEvento } from 'app/shared/model/evento.model';
import { TipoLocal } from 'app/shared/model/enumerations/tipo-local.model';

export interface ILocal {
  id?: number;
  nomeLocal?: string;
  tipoLocal?: TipoLocal;
  evento?: IEvento;
}

export class Local implements ILocal {
  constructor(public id?: number, public nomeLocal?: string, public tipoLocal?: TipoLocal, public evento?: IEvento) {}
}
