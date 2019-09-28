import { Website } from './website';

export class Item {
  id: number;
  website: Website;
  ref: string;
  title: string;
  url: string;
  body: string;
  createdAt: Date;
  updatedAt: Date;

  constructor() {
    this.website = new Website();
  }
}
