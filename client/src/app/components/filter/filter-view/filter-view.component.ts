import { AuthService } from './../../../services/auth.service';
import { Filter } from './../../../models/filter';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import * as moment from 'moment';

@Component({
  selector: 'app-filter-view',
  templateUrl: './filter-view.component.html',
  styleUrls: ['./filter-view.component.css']
})
export class FilterViewComponent implements OnInit {

  filter: Filter;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router,
    private authService: AuthService
  ) {
    this.filter = new Filter();
    this.route.params.subscribe(params => {
      this.http.get(
        '/api/filter/' + params['id']
      ).subscribe((filter: Filter) => {
        this.filter = filter;
      });
    });
  }

  ngOnInit() {
  }

}
