import { User } from './user';
import { Filter } from './filter';

export class Selection {
  id: number;
  name: string;
  user: User;
  filters: Filter[];

  constructor() {
    this.filters = [];
    // this.user = new User();
  }
}
