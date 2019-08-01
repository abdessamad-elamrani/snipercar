import { Component, OnInit } from '@angular/core';
import { Filter } from './../../../models/filter';
@Component({
  selector: 'app-filter-view',
  templateUrl: './filter-view.component.html',
  styleUrls: ['./filter-view.component.css']
})
export class FilterViewComponent implements OnInit {
  public filter: Filter;

  constructor() {
    this.filter = {
      id: 1,
      url: 'https://www.google.com/',
      websiteId: 1
    };
  }

  ngOnInit() {
  }

}
