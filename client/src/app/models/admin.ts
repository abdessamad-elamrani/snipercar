export class Admin {
  id: number;
  active: boolean;
  email: string;
  // emailNotif: boolean;
  fullname: string;
  password: string;
  passwordChange: boolean;
  newPassword: string;
  newPasswordConfirm: string;
  phone: string;
  role: string;
  salt: string;
  // smsNotif: boolean;
  username: string;
  // companyId: number;
  // company: Company;
  // currentSelectionId: number;
  // currentSelection: Selection;

  constructor() {
    // this.company = new Company();
    // this.currentSelection = new Selection();
    this.passwordChange = false;
    this.role = 'ADMIN';
  }
}

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
  // company: Company;
  phone: string;
  // smsNotif: boolean;
  // emailNotif: boolean;
  selections: Selection[];
  // currentSelection: Selection;
  active: boolean;

  constructor() {
    this.passwordChange = false;
    this.active = true;
    this.role = 'ADMIN';
    // this.company = new Company();
    // this.currentSelection = new Selection();
  }
}
