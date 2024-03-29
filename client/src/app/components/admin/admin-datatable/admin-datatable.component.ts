import { AuthService } from '../../../services/auth.service';
import { Observable } from 'rxjs/Observable';
import { DataTablesResponse } from './../../../models/DataTablesResponse';
import { HttpClient } from '@angular/common/http';
import { Admin } from './../../../models/admin';
import { Component, OnInit, OnDestroy, ViewChild, NgZone } from '@angular/core';
import { DataTableDirective } from 'angular-datatables';
import { Router } from '@angular/router';
import * as moment from 'moment';
import { PNotifyService } from '../../../services/pnotify.service';
import { NgxRolesService, NgxPermissionsService } from 'ngx-permissions';

@Component({
  selector: 'app-admin-datatable',
  templateUrl: './admin-datatable.component.html',
  styleUrls: ['./admin-datatable.component.css']
})
export class AdminDatatableComponent implements OnInit, OnDestroy {

  filter = {
    name: '',
    description: '',
  };
  staticFilter = {
    name: '',
    description: '',
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
  admins: any[];
  pnotify = undefined;
  constructor(
    private http: HttpClient,
    private router: Router,
    private zone: NgZone,
    private authService: AuthService,
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
        dataTablesParameters.filter = this.staticFilter;
        this.http.post<DataTablesResponse>(
          '/api/admin/datatables',
          dataTablesParameters,
          {}
        ).subscribe((resp: any) => {
          this.admins = resp.data;
          const dta = [];
          this.admins.forEach(admin => {
            dta.push({
              id: admin.id,
              name: admin.name,
              email: admin.email,
              phone: admin.phone,
              role: admin.role,
              active: admin.active ? 'Yes' : 'No',
              actions: () => {
                let actions = `
                  <a class="btn btnAction btnNavigate" data-url="/admin/view/${admin.id}">
                    <i class="fa fa-search fa-2x" aria-hidden="true"></i>
                  </a>
                  <a class="btn btnAction btnNavigate" data-url="/admin/edit/${admin.id}">
                    <i class="fa fa-pencil fa-2x" aria-hidden="true"></i>
                  </a>
                `;
                if (this.authService.sessionContextValue.user.id !== admin.id) {
                  actions += `
                    <button class="btn btnAction btnDelete" data-admin-id="${admin.id}">
                      <i class="fa fa-trash fa-2x" aria-hidden="true"></i>
                    </button>
                  `;
                }
                return actions;
              }
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
        $(nRow).attr('id', 'admin_' + aData.id);
      },
      columns: [
        { data: 'name' },
        { data: 'email' },
        { data: 'phone' },
        { data: 'role' },
        { data: 'active' },
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
      this.onDelete($(event.currentTarget).data('adminId'));
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
      .delete('/api/admin/' + id)
      .subscribe(
        result => {
          document.getElementById('admin_' + id).remove();
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
    this.staticFilter = this.filter;
    this.datatableElement.dtInstance.then((dtInstance: DataTables.Api) => {
      dtInstance.draw();
    });
  }

}
