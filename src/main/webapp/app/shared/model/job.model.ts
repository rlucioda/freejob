import { Moment } from 'moment';
import { IAgencia } from 'app/shared/model/agencia.model';
import { TipoPagamento } from 'app/shared/model/enumerations/tipo-pagamento.model';

export interface IJob {
  id?: number;
  nomeJob?: string;
  valorHora?: number;
  tempoEvento?: number;
  observacao?: string;
  dataPagamento?: Moment;
  tipoPagamento?: TipoPagamento;
  statusPagamento?: boolean;
  agencia?: IAgencia;
}

export class Job implements IJob {
  constructor(
    public id?: number,
    public nomeJob?: string,
    public valorHora?: number,
    public tempoEvento?: number,
    public observacao?: string,
    public dataPagamento?: Moment,
    public tipoPagamento?: TipoPagamento,
    public statusPagamento?: boolean,
    public agencia?: IAgencia
  ) {
    this.statusPagamento = this.statusPagamento || false;
  }
}
