import { AuthService } from './../../../services/auth.service';
import { Selection } from './../../../models/selection';
import { User } from './../../../models/user';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit, AfterViewInit } from '@angular/core';
import { PNotifyService } from '../../../services/pnotify.service';

@Component({
  selector: 'app-selection-edit',
  templateUrl: './selection-edit.component.html',
  styleUrls: ['./selection-edit.component.css']
})
export class SelectionEditComponent implements OnInit, AfterViewInit {

  isAdmin: boolean;
  selection: Selection;
  isDefault: boolean;
  filters: {};
  filtersData: any[];
  filtersOptions: {};
  pnotify = undefined;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router,
    private authService: AuthService,
    pnotifyService: PNotifyService
  ) {
    this.isAdmin = this.authService.hasRole('ROLE_ADMIN');
    this.selection = new Selection();
    this.route.params.subscribe(params => {
      this.http.get(
        '/api/selection/' + params['id']
      ).subscribe((selection: Selection) => {
        this.isDefault = selection.isDefault;
        let selectedFilters = [];
        this.selection = selection;
        this.selection.filters.forEach((filter, index) => {
          selectedFilters.push(filter.id);
        });
        this.http.get(
          '/api/filter'
        ).subscribe((filters: any[]) => {
          this.filters = {};
          this.filtersData = [];
          filters.forEach((filter, index) => {
            this.filters[filter.id] = filter;
            this.filtersData.push({
              id: filter.id,
              text: filter.name,
              selected: selectedFilters.includes(filter.id) ? true : false
            });
          });
        });
      });
    });
    this.filtersOptions = { multiple: 'true' };
    this.pnotify = pnotifyService.getPNotify();
  }

  ngOnInit() {
    // console.error('isDefault', $('#isDefault').length);
    // $('#isDefault').select2();
  }

  ngAfterViewInit() {
    // console.error('isDefault', $('#isDefault').length);
    // $('#isDefault').select2();
  }

  onFiltersCHange(e: any) {
    let filters = [];
    e.value.forEach((filterId, index) => {
      filters.push(this.filters[filterId]);
    });
    this.selection.filters = filters;
  }

  onSubmit(): void {
    this.http.put(
      '/api/selection/' + this.selection.id,
      this.selection
    ).subscribe(
      (selection: Selection) => {
        this.selection = selection;
        this.router.navigate(['/selection/view', this.selection.id]);
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
