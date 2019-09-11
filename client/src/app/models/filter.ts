import { Website } from './website';

export class Filter {
  id: number;
  url: string;
  name: string;
  website: Website;

  constructor() {
    this.website = new Website();
  }
}
