import { ILocal } from 'app/shared/model/local.model';

export interface IEndereco {
  id?: number;
  rua?: string;
  numero?: string;
  complemento?: string;
  bairro?: string;
  cep?: string;
  cidade?: string;
  estado?: string;
  local?: ILocal;
}

export class Endereco implements IEndereco {
  constructor(
    public id?: number,
    public rua?: string,
    public numero?: string,
    public complemento?: string,
    public bairro?: string,
    public cep?: string,
    public cidade?: string,
    public estado?: string,
    public local?: ILocal
  ) {}
}
