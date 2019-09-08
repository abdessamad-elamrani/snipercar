import { Observable } from 'rxjs/Observable';
import { DataTablesResponse } from './../../../models/DataTablesResponse';
import { HttpClient } from '@angular/common/http';
import { Filter } from './../../../models/filter';
import { Component, OnInit, OnDestroy, ViewChild, NgZone } from '@angular/core';
import { DataTableDirective } from 'angular-datatables';
import { Router } from '@angular/router';
import * as moment from 'moment';
import { PNotifyService } from '../../../services/pnotify.service';

@Component({
  selector: 'app-filter-datatable',
  templateUrl: './filter-datatable.component.html',
  styleUrls: ['./filter-datatable.component.css']
})
export class FilterDatatableComponent implements OnInit, OnDestroy {

  search = {
    name: '',
  };
  staticSearch = {
    name: '',
  };

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
  filters: any[];
  pnotify = undefined;
  constructor(
    private http: HttpClient,
    private router: Router,
    private zone: NgZone,
    pnotifyService: PNotifyService
  ) {
    // this.filterForm = new FormGroup();
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
          '/api/filter/datatables',
          dataTablesParameters,
          {}
        ).subscribe((resp: any) => {
          this.filters = resp.data;
          const dta = [];
          this.filters.forEach(filter => {
            dta.push({
              id: filter.id,
              name: filter.name,
              url: filter.url,
              websiteName: filter.website.name,
              actions: `
                <a class="btn btnAction btnNavigate" data-url="/filter/view/${filter.id}">
                  <i class="fa fa-search fa-2x" aria-hidden="true"></i>
                </a>
                <a class="btn btnAction btnNavigate" data-url="/filter/edit/${filter.id}">
                  <i class="fa fa-pencil fa-2x" aria-hidden="true"></i>
                </a>
                <button class="btn btnAction btnDelete" data-filter-id="${filter.id}">
                  <i class="fa fa-trash fa-2x" aria-hidden="true"></i>
                </button>
              `
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
        $(nRow).attr('id', 'filter_' + aData.id);
      },
      columns: [
        { data: 'name' },
        { data: 'url' },
        { data: 'websiteName' },
        { data: 'actions' },
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
    $('table').on('click', '.btnDelete', (event) => {
      this.onDelete($(event.currentTarget).data('filterId'));
    });
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

  onDelete(id) {
    this.pnotify.notice({
      title: 'Confirmation',
      text: 'Are you sure to delete this element ?',
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
                this.delete(id);
                notice.close();
              }
            },
            {
              text: 'Cancel',
              addClass: 'btn btn-default',
              click: (notice) => {
                notice.close();
                notice.fire('pnotify.cancel', { notice });
              }
            }
          ]
        }
      }
    });
  }

  delete(id) {
    this.http
      .delete('/api/filter/' + id)
      .subscribe(
        result => {
          document.getElementById('filter_' + id).remove();
        },
        error => {
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

  onFilter(): void {
    this.staticSearch = this.search;
    this.datatableElement.dtInstance.then((dtInstance: DataTables.Api) => {
      dtInstance.draw();
    });
  }

}
