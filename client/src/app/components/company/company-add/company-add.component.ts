import { Component, OnInit } from '@angular/core';
import { Company } from './../../../models/company';
import { Select2OptionData } from 'ng2-select2';

@Component({
  selector: 'app-company-add',
  templateUrl: './company-add.component.html',
  styleUrls: ['./company-add.component.css']
})
export class CompanyAddComponent implements OnInit {

  public compnay: Company;
  public slaData: Array<Select2OptionData>;

  dateTimeFilter = (d: Date): boolean => {
    return true;
  }

  constructor() {
    this.compnay = new Company();
  }

  ngOnInit() {
    this.slaData = [
      {
        id: '1',
        text: 'Sla 1'
      },
      {
        id: '2',
        text: 'Sla 2'
      },
    ];
  }

}
