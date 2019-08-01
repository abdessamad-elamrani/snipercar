import { Component, OnInit } from '@angular/core';
import { Sla } from './../../../models/sla';

@Component({
  selector: 'app-sla-edit',
  templateUrl: './sla-edit.component.html',
  styleUrls: ['./sla-edit.component.css']
})
export class SlaEditComponent implements OnInit {
  public sla: Sla;

  constructor() {
    this.sla = {
      id: 1,
      name: 'Test',
      description: 'Lorem ipsum dolor sit amet consectetur adipisicing elit. Sequi quo quidem facilis iure! Eum, dolore atque ratione accusamus aliquam eos et ab commodi optio molestiae voluptatibus alias. Ipsam, suscipit quia.',
      latency: 5,
      price: 100
    };
  }

  ngOnInit() {
  }

}
