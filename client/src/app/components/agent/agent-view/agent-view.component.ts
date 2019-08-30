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
    this.agent = new User();
  }

  ngOnInit() {
  }

}
