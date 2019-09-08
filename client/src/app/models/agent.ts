import { Company } from './company';
import { Selection } from './selection';

export class Agent {
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
  // currentSelectionId: number;
  active: boolean;

  constructor() {
    this.passwordChange = false;
    this.active = true;
    this.role = 'AGENT';
    // this.company = new Company();
    // this.currentSelection = new Selection();
  }
}
