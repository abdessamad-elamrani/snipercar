import { Component, OnInit } from '@angular/core';
import { User } from './../../../models/user';
@Component({
  selector: 'app-agent-view',
  templateUrl: './agent-view.component.html',
  styleUrls: ['./agent-view.component.css']
})
export class AgentViewComponent implements OnInit {
  public agent: User;

  constructor() {
    this.agent = {
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
