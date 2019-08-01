import { Component, OnInit } from '@angular/core';
import { User } from './../../../models/user';
@Component({
  selector: 'app-admin-view',
  templateUrl: './admin-view.component.html',
  styleUrls: ['./admin-view.component.css']
})
export class AdminViewComponent implements OnInit {
  public admin: User;

  constructor() {
    this.admin = {
      id: 1,
      active: false,
      email: 'test@gmail.com',
      emailNotif: true,
      firstname: 'test',
      lastname: 'Test',
      password: '',
      phone: '062582586589',
      role: '1',
      salt: 'test',
      smsNotif: true,
      username: 'username',
      companyId: 1,
      currentSelectionId: 1,
    };
  }


  ngOnInit() {

  }

}
