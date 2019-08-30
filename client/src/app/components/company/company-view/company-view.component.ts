import { AuthService } from './../../../services/auth.service';
import { Company } from './../../../models/company';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import * as moment from 'moment';

@Component({
  selector: 'app-company-view',
  templateUrl: './company-view.component.html',
  styleUrls: ['./company-view.component.css']
})
export class CompanyViewComponent implements OnInit {

  company: Company;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router,
    private authService: AuthService
  ) {
    this.company = new Company();
    this.route.params.subscribe(params => {
      this.http.get(
        '/api/company/' + params['id']
      ).subscribe((company: Company) => {
        this.company = company;
        this.company.expiration = moment(company.expiration).format('YYYY-MM-DD');
      });
    });
  }

  ngOnInit() {
  }

}
