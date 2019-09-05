import { Website } from './website';

export class Filter {
  id: number;
  url: string;
  name: string;
  // websiteId: number;
  website: Website;

  constructor() {
    // this.website = new Website();
  }
}
