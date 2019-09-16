import { User } from './user';
import { Filter } from './filter';

export class Selection {
  id: number;
  name: string;
  user: User;
  isDefault: boolean;
  filters: Filter[];

  constructor() {
    this.user = new User();
    this.isDefault = false;
    this.filters = [];
  }
}
