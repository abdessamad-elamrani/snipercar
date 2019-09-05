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
  // currectSelection: Selection;

  constructor() {
    // this.company = new Company();
    // this.currectSelection = new Selection();
    this.passwordChange = false;
    this.role = 'ADMIN';
  }
}
