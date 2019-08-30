import { AuthService } from './../../../services/auth.service';
import { Company } from './../../../models/company';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { PNotifyService } from '../../../services/pnotify.service';
import { Select2OptionData } from 'ng2-select2';
import { Sla } from './../../../models/sla';
import * as moment from 'moment';

@Component({
  selector: 'app-company-edit',
  templateUrl: './company-edit.component.html',
  styleUrls: ['./company-edit.component.css']
})
export class CompanyEditComponent implements OnInit {

  company: Company;
  slas: {};
  pnotify = undefined;
  public slasData: Array<any>;

  dateTimeFilter = (d: Date): boolean => {
    return true;
  }

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router,
    private authService: AuthService,
    pnotifyService: PNotifyService
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
    this.http.get(
      '/api/sla/select2'
    ).subscribe((slas: Sla[]) => {
      this.slas = {};
      this.slasData = slas.map((sla, index) => {
        this.slas[sla.id] = sla;
        return {
          id: sla.id,
          text: sla.name,
          selected: (sla.id == this.company.sla.id) ? true : false
        };
      });
    });
    this.pnotify = pnotifyService.getPNotify();
  }

  ngOnInit() {
  }

  slaChanged(e: any) {
    this.company.sla = this.slas[e.value];
  }

  onSubmit(): void {
    this.company.expiration = moment(this.company.expiration).local().format('YYYY-MM-DD');
    this.http.put(
      '/api/company/' + this.company.id,
      this.company
    ).subscribe(
      (company: Company) => {
        this.company = company;
        this.router.navigate(['/company/view', this.company.id]);
      },
      (error) => {
        this.pnotify.error({
          title: 'Erreur',
          text: 'An error has occured !',
          stack: {
            firstpos1: 70, firstpos2: 10,
            modal: true,
            overlay_close: true
          },
          hide: false,
          modules: {
            Confirm: {
              confirm: true,
              buttons: [
                {
                  text: 'Ok',
                  addClass: 'btn btn-chico',
                  click: notice => {
                    notice.close();
                  }
                }
              ]
            }
          }
        });
      }
    );
  }

}
