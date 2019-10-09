import { Company } from './../../../models/company';
import { AuthService } from './../../../services/auth.service';
import { Observable } from 'rxjs/Observable';
import { DataTablesResponse } from './../../../models/DataTablesResponse';
import { HttpClient, HttpParams } from '@angular/common/http';
import { User } from './../../../models/user';
import { Component, OnInit, OnDestroy, ViewChild, NgZone } from '@angular/core';
import { DataTableDirective } from 'angular-datatables';
import { Router } from '@angular/router';
import * as moment from 'moment';
import { PNotifyService } from '../../../services/pnotify.service';

@Component({
  selector: 'app-user-item-datatable',
  templateUrl: './user-item-datatable.component.html',
  styleUrls: ['./user-item-datatable.component.css']
})
export class UserItemDatatableComponent implements OnInit, OnDestroy {

  search = {
    companyId: 0,
    agentId: 0,
    itemTitle: '',
  };
  staticSearch = {
    companyId: 0,
    agentId: 0,
    itemTitle: '',
  };

  public companyData: any[];
  public agentData: any[];

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
  userItems: any[];
  pnotify = undefined;
  constructor(
    private http: HttpClient,
    private authService: AuthService,
    private router: Router,
    private zone: NgZone,
    pnotifyService: PNotifyService
  ) {
    this.staticSearch = this.search;
    this.initCompany();
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
          '/api/user-item/datatables',
          dataTablesParameters,
          {}
        ).subscribe((resp: any) => {
          this.userItems = resp.data;
          const dta = [];
          this.userItems.forEach(data => {
            let smsData = data.phone + ' (' +
              (data.smsNotif ? (data.smsSent ? 'Success' : 'Failure') : 'Not Sent') + ')';
            dta.push({
              id: data.id,
              companyName: data.companyName,
              agentName: data.agentName,
              itemTitle: data.itemTitle,
              smsData: data.phone + ' (' +
                (data.smsNotif ? (data.smsSent ? 'Success' : 'Failure') : 'Not Sent') + ')',
              smsLog: data.smsLog,
              emailData: data.email + ' (' +
                (data.emailNotif ? (data.emailSent ? 'Success' : 'Failure') : 'Not Sent') + ')',
              emailLog: data.emailLog,
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
        $(nRow).attr('id', 'userItem_' + aData.id);
      },
      columns: [
        { data: 'companyName' },
        { data: 'agentName' },
        { data: 'itemTitle' },
        { data: 'smsData' },
        { data: 'smsLog' },
        { data: 'emailData' },
        { data: 'emailLog' },
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

  initCompany() {
    this.http.get(
      '/api/company/select2'
    ).subscribe((companies: any[]) => {
      this.companyData = companies.map((company, index) => {
        return {
          id: company.id,
          text: company.name,
        };
      });
      this.companyData.unshift({
        id: 0,
        text: '--',
        selected: true
      });
      this.initAgent();
    });
  }
  companyChanged(e: any) {
    this.search.companyId = e.value;
    this.initAgent();
  }

  initAgent() {
    const params = new HttpParams({
      fromObject: {
        companyId: this.search.companyId + ''
      }
    });
    this.http.get(
      '/api/agent/select2',
      { params }
    ).subscribe((companies: any[]) => {
      this.agentData = companies.map((agent, index) => {
        return {
          id: agent.id,
          text: agent.name,
        };
      });
      this.agentData.unshift({
        id: 0,
        text: '--',
        selected: true
      });
    });
  }
  agentChanged(e: any) {
    this.search.agentId = e.value;
  }

  onSearch(): void {
    this.staticSearch = this.search;
    this.datatableElement.dtInstance.then((dtInstance: DataTables.Api) => {
      dtInstance.draw();
    });
  }

}
