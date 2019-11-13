import { IJob } from 'app/shared/model/job.model';

export interface IAgencia {
  id?: number;
  nomeAgencia?: string;
  contatoAgencia?: string;
  email?: string;
  jobs?: IJob[];
}

export class Agencia implements IAgencia {
  constructor(
    public id?: number,
    public nomeAgencia?: string,
    public contatoAgencia?: string,
    public email?: string,
    public jobs?: IJob[]
  ) {}
}
