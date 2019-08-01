import { Component, OnInit } from '@angular/core';
import { Company } from './../../../models/company';

@Component({
  selector: 'app-company-view',
  templateUrl: './company-view.component.html',
  styleUrls: ['./company-view.component.css']
})
export class CompanyViewComponent implements OnInit {
  public compnay: Company;
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
  }

}
