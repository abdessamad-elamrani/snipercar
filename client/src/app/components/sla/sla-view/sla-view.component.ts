import { AuthService } from './../../../services/auth.service';
import { Sla } from './../../../models/sla';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-sla-view',
  templateUrl: './sla-view.component.html',
  styleUrls: ['./sla-view.component.css']
})
export class SlaViewComponent implements OnInit {

  sla: Sla;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router,
    private authService: AuthService
  ) {
    this.sla = new Sla();
    this.route.params.subscribe(params => {
      this.http.get(
        '/api/sla/' + params['id']
      ).subscribe((sla: Sla) => {
        this.sla = sla;
      });
    });
  }

  ngOnInit() {
  }

}
