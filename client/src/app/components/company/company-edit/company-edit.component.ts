import { Component, OnInit } from '@angular/core';
import { Company } from './../../../models/company';
import { Select2OptionData } from 'ng2-select2';

@Component({
  selector: 'app-company-edit',
  templateUrl: './company-edit.component.html',
  styleUrls: ['./company-edit.component.css']
})
export class CompanyEditComponent implements OnInit {

  public compnay: Company;
  public slaData: Array<Select2OptionData>;

  dateTimeFilter = (d: Date): boolean => {
    return true;
  }

  constructor() {
    this.compnay = {
      id: 1,
      active: true,
      address: 'Test',
      email: 'Test@gmail.com',
      expiration: '01/12/2019',
      name: 'test',
      phone: '06258526485',
      slaId: 1
    };
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
