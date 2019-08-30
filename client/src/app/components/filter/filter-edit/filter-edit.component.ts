import { Component, OnInit } from '@angular/core';
import { Filter } from './../../../models/filter';
import { Select2OptionData } from 'ng2-select2';

@Component({
  selector: 'app-filter-edit',
  templateUrl: './filter-edit.component.html',
  styleUrls: ['./filter-edit.component.css']
})
export class FilterEditComponent implements OnInit {

  public filter: Filter;
  public websiteData: Array<Select2OptionData>;

  constructor() {
    this.filter = new Filter();
  }

  ngOnInit() {

    this.websiteData = [
      {
        id: '1',
        text: 'goolge'
      },
      {
        id: '2',
        text: 'website 2'
      },
    ];
  }

}
