import { Moment } from 'moment';
import { ILabel } from 'app/shared/model/label.model';
import { IBankAccount } from 'app/shared/model/bank-account.model';

export interface IOperation {
  id?: number;
  date?: Moment;
  description?: string;
  amount?: number;
  labels?: ILabel[];
  bankAccount?: IBankAccount;
}

export class Operation implements IOperation {
  constructor(
    public id?: number,
    public date?: Moment,
    public description?: string,
    public amount?: number,
    public labels?: ILabel[],
    public bankAccount?: IBankAccount
  ) {}
}
