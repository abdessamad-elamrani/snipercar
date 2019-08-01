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
  companyId: number;
  currentSelectionId: number;

  constructor() { }
}
