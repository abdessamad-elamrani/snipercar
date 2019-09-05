import { Sla } from './sla';

export class Company {
  id: number;
  active: boolean;
  address: string;
  email: string;
  expiration: string;
  name: string;
  phone: string;
  // slaId: number;
  sla: Sla;

  constructor() {
    // this.sla = new Sla();
  }
}
