import { Component, OnInit } from '@angular/core';
import { Filter } from './../../../models/filter';
import { Select2OptionData } from 'ng2-select2';

@Component({
  selector: 'app-filter-add',
  templateUrl: './filter-add.component.html',
  styleUrls: ['./filter-add.component.css']
})
export class FilterAddComponent implements OnInit {

  public filter: Filter;
  public websiteData: Array<Select2OptionData>;
  constructor() {
    this.filter = new Filter();
  }

  ngOnInit() {
    this.websiteData = [
      {
        id: '1',
        text: 'website 1'
      },
      {
        id: '2',
        text: 'website 2'
      },
    ];
  }

}
