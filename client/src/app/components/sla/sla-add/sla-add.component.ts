import { Component, OnInit } from '@angular/core';
import { Sla } from './../../../models/sla';

@Component({
  selector: 'app-sla-add',
  templateUrl: './sla-add.component.html',
  styleUrls: ['./sla-add.component.css']
})
export class SlaAddComponent implements OnInit {
  public sla: Sla;

  constructor() {
    this.sla = new Sla();
  }

  ngOnInit() {
  }

}
