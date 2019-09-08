import { AuthService } from './../../../services/auth.service';
import { Selection } from './../../../models/selection';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-selection-view',
  templateUrl: './selection-view.component.html',
  styleUrls: ['./selection-view.component.css']
})
export class SelectionViewComponent implements OnInit {

  selection: Selection;
  filters: {};
  filtersData: any[];
  filtersOptions: {};
  pnotify = undefined;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router,
    private authService: AuthService
  ) {
    this.selection = new Selection();
    this.route.params.subscribe(params => {
      this.http.get(
        '/api/selection/' + params['id']
        ).subscribe((selection: Selection) => {
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
    this.filtersOptions = { multiple: 'true', disabled: true };
  }

  ngOnInit() {
  }

}
