import { Company } from './company';
import { Selection } from './selection';

export class User {
  id: number;
  name: string;
  username: string;
  email: string;
  salt: string;
  password: string;
  passwordChange: boolean;
  newPassword: string;
  newPasswordConfirm: string;
  role: string;
  // companyId: number;
  company: Company;
  phone: string;
  smsNotif: boolean;
  emailNotif: boolean;
  selections: Selection[];
  // currentSelectionId: number;
  currectSelection: Selection;
  active: boolean;

  constructor() {
    this.passwordChange = false;
    this.active = true;
    // this.company = new Company();
    // this.currectSelection = new Selection();
  }
}
