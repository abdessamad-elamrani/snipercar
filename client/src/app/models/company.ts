import { Sla } from './sla';

export class Company {
  id: number;
  active: boolean;
  address: string;
  email: string;
  expiration: string;
  name: string;
  phone: string;
  sla: Sla;

  constructor() {
    // this.sla = new Sla();
  }
}
