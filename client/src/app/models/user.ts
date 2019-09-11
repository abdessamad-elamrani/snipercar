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
  company: Company;
  phone: string;
  smsNotif: boolean;
  emailNotif: boolean;
  selections: Selection[];
  currentSelection: Selection;
  active: boolean;

  constructor() {
    this.passwordChange = false;
    this.active = true;
    this.role = 'USER';
    this.company = new Company();
    // this.currentSelection = new Selection();
  }
}
