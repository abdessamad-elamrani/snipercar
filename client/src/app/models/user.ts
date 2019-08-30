import { Company } from './company';
import { Selection } from './selection';

export class User {
  id: number;
  active: boolean;
  email: string;
  emailNotif: boolean;
  firstname: string;
  lastname: string;
  password: string;
  phone: string;
  role: string;
  salt: string;
  smsNotif: boolean;
  username: string;
  // companyId: number;
  company: Company;
  // currentSelectionId: number;
  currectSelection: Selection;

  constructor() { 
    this.company = new Company();
    this.currectSelection = new Selection();
  }
}
