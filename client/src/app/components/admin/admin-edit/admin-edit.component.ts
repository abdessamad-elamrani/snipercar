import { Component, OnInit } from '@angular/core';
import { User } from './../../../models/user';
import { Select2OptionData } from 'ng2-select2';

@Component({
  selector: 'app-admin-edit',
  templateUrl: './admin-edit.component.html',
  styleUrls: ['./admin-edit.component.css']
})
export class AdminEditComponent implements OnInit {
  public admin: User;
  public roleData: Array<Select2OptionData>;
  public companyData: Array<Select2OptionData>;
  public selectionData: Array<Select2OptionData>;

  constructor() {
    this.admin = new User();
  }

  ngOnInit() {

    this.roleData = [
      {
        id: '1',
        text: 'rôle 1'
      },
      {
        id: '2',
        text: 'rôle 2'
      },
    ];
    this.companyData = [
      {
        id: '1',
        text: 'Company 1'
      },
      {
        id: '2',
        text: 'Company 2'
      },
    ];
    this.selectionData = [
      {
        id: '1',
        text: 'Selection 1'
      },
      {
        id: '2',
        text: 'Selection 2'
      },
    ];
  }

}
