import { Website } from './../../../models/website';
import { AuthService } from './../../../services/auth.service';
import { Observable } from 'rxjs/Observable';
import { DataTablesResponse } from './../../../models/DataTablesResponse';
import { HttpClient, HttpParams } from '@angular/common/http';
// import { Filter } from './../../../models/filter-item';
import { Component, OnInit, OnDestroy, ViewChild, NgZone } from '@angular/core';
import { DataTableDirective } from 'angular-datatables';
import { Router } from '@angular/router';
import * as moment from 'moment';
import { PNotifyService } from '../../../services/pnotify.service';

@Component({
  selector: 'app-filter-item-datatable',
  templateUrl: './filter-item-datatable.component.html',
  styleUrls: ['./filter-item-datatable.component.css']
})
export class FilterItemDatatableComponent implements OnInit, OnDestroy {

  search = {
    websiteId: 0,
    filterId: 0,
    itemTitle: '',
  };
  staticSearch = {
    websiteId: 0,
    filterId: 0,
    itemTitle: '',
  };

  public websiteData: any[];
  public filterData: any[];

  @ViewChild(DataTableDirective, { static: false })
  datatableElement: DataTableDirective;

  // ----- Start Option Scrollbar -----------
  isScrollbar: true;
  scrollbarOptions = {
    axis: 'y',
    theme: 'minimal-dark'
  };
  // ----- End Option Scrollbar -----------

  dtOptions: any = {};
  filterItems: any[];
  pnotify = undefined;
  constructor(
    private http: HttpClient,
    private authService: AuthService,
    private router: Router,
    private zone: NgZone,
    pnotifyService: PNotifyService
  ) {
    this.staticSearch = this.search;
    this.initWebsite();
    this.pnotify = pnotifyService.getPNotify();
  }

  ngOnInit(): void {
    this.dtOptions = {
      searching: false,
      pageLength: 5,
      serverSide: true,
      processing: true,
      lengthMenu: [[5, 10, 25, 50, 100], [5, 10, 25, 50, 100]],
      order: [[0, 'asc']],
      ordering: false,
      columnDefs: [
        { 'orderable': false, 'targets': 0 }
      ],
      ajax: (dataTablesParameters: any, callback) => {
        dataTablesParameters.filter = this.staticSearch;
        this.http.post<DataTablesResponse>(
          '/api/filter-item/datatables',
          dataTablesParameters,
          {}
        ).subscribe((resp: any) => {
          this.filterItems = resp.data;
          const dta = [];
          this.filterItems.forEach(data => {
            dta.push({
              id: data.id,
              websiteName: data.websiteName,
              filterName: data.filterName,
              itemTitle: data.itemTitle,
              firstParse: data.firstParse ? 'Yes' : 'No',
              createdAt: data.createdAt,
            });
          });
          callback({
            recordsTotal: resp.recordsTotal,
            recordsFiltered: resp.recordsFiltered,
            data: dta
          });
        });
      },
      fnCreatedRow: function (nRow, aData, iDataIndex) {
        $(nRow).attr('id', 'filterItem_' + aData.id);
      },
      columns: [
        { data: 'websiteName' },
        { data: 'filterName' },
        { data: 'itemTitle' },
        { data: 'firstParse' },
        { data: 'createdAt' },
      ],
      dom: '<t> <"row" <"col-md-4"l><"col-md-8"p>>',
      language: {
        'sProcessing': 'Processing ...',
        'sSearch': 'Search&nbsp;:',
        'sLengthMenu': 'Display _MENU_ elements',
        'sInfo': 'Display of element _START_ to _END_ from _TOTAL_ elements',
        'sInfoEmpty': 'Display of element 0 to 0 from 0 elements',
        'sInfoFiltered': '(_MAX_ elements in total)',
        'sInfoPostFix': '',
        'sLoadingRecords': 'Loading ...',
        'sZeroRecords': 'No element to display',
        'sEmptyTable': 'No data available in table',
        'oPaginate': {
          'sFirst': 'First',
          'sPrevious': 'Previous',
          'sNext': 'Next',
          'sLast': 'Last'
        },
        'oAria': {
          'sSortAscending': ': activate for ascendent order',
          'sSortDescending': ': activate for descendent order'
        }
      },
      // Use this attribute to enable the responsive extension
      responsive: true
    };
    $('table').on('click', '.btnNavigate', (event) => {
      this.onNavigate($(event.currentTarget).data('url'));
    });
  }

  ngOnDestroy(): void {
    $.fn['dataTable'].ext.search.pop();
    $('table').off();
  }

  onNavigate(url) {
    this.zone.run(() => this.router.navigateByUrl(url));
  }

  initWebsite() {
    this.http.get(
      '/api/website/select2'
    ).subscribe((companies: any[]) => {
      this.websiteData = companies.map((website, index) => {
        return {
          id: website.id,
          text: website.name,
        };
      });
      this.websiteData.unshift({
        id: 0,
        text: '--',
        selected: true
      });
      this.initFilter();
    });
  }
  websiteChanged(e: any) {
    this.search.websiteId = e.value;
    this.initFilter();
  }

  initFilter() {
    const params = new HttpParams({
      fromObject: {
        websiteId: this.search.websiteId + ''
      }
    });
    this.http.get(
      '/api/filter/select2',
      { params }
    ).subscribe((companies: any[]) => {
      this.filterData = companies.map((filter, index) => {
        return {
          id: filter.id,
          text: filter.name,
        };
      });
      this.filterData.unshift({
        id: 0,
        text: '--',
        selected: true
      });
    });
  }
  filterChanged(e: any) {
    this.search.filterId = e.value;
  }

  onSearch(): void {
    this.staticSearch = this.search;
    this.datatableElement.dtInstance.then((dtInstance: DataTables.Api) => {
      dtInstance.draw();
    });
  }

}
