import { User } from './user';

export class SessionContext {
  token: string;
  expiration: number;
  roles: string[];
  user: User;

  constructor() { 
    this.user = new User();
  }
}
