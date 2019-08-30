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
    this.admin = new User();
  }


  ngOnInit() {

  }

}
