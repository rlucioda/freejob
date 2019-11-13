import { Moment } from 'moment';
import { IJob } from 'app/shared/model/job.model';
import { TipoEvento } from 'app/shared/model/enumerations/tipo-evento.model';

export interface IEvento {
  id?: number;
  nomeEvento?: string;
  descricaEvento?: string;
  dataInicio?: Moment;
  dataFim?: Moment;
  statusJob?: boolean;
  tipoEvento?: TipoEvento;
  job?: IJob;
}

export class Evento implements IEvento {
  constructor(
    public id?: number,
    public nomeEvento?: string,
    public descricaEvento?: string,
    public dataInicio?: Moment,
    public dataFim?: Moment,
    public statusJob?: boolean,
    public tipoEvento?: TipoEvento,
    public job?: IJob
  ) {
    this.statusJob = this.statusJob || false;
  }
}
