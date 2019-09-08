import { AuthService } from './../../../services/auth.service';
import { Filter } from './../../../models/filter';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { PNotifyService } from '../../../services/pnotify.service';
import { Select2OptionData } from 'ng2-select2';
import { Website } from './../../../models/website';
import * as moment from 'moment';

@Component({
  selector: 'app-filter-add',
  templateUrl: './filter-add.component.html',
  styleUrls: ['./filter-add.component.css']
})
export class FilterAddComponent implements OnInit {

  filter: Filter;
  websites: {};
  pnotify = undefined;
  public websitesData: Array<any>;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router,
    private authService: AuthService,
    pnotifyService: PNotifyService
  ) {
    this.filter = new Filter();
    this.http.get(
      '/api/website/select2'
    ).subscribe((websites: Website[]) => {
      this.websites = {};
      this.websitesData = websites.map((website, index) => {
        this.websites[website.id] = website;
        return {
          id: website.id,
          text: website.name,
          selected: (website.id == this.filter.website.id) ? true : false
        };
      });
    });
    this.pnotify = pnotifyService.getPNotify();
  }

  ngOnInit() {
  }

  websiteChanged(e: any) {
    this.filter.website = this.websites[e.value];
  }

  onSubmit(): void {
    this.http.post(
      '/api/filter',
      this.filter
    ).subscribe(
      (filter: Filter) => {
        this.filter = filter;
        this.router.navigate(['/filter/view', this.filter.id]);
      },
      (error) => {
        this.pnotify.error({
          title: 'Error',
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
