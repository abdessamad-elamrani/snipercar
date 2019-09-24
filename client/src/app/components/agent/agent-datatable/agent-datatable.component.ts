import { Company } from './../../../models/company';
import { Observable } from 'rxjs/Observable';
import { DataTablesResponse } from './../../../models/DataTablesResponse';
import { HttpClient } from '@angular/common/http';
import { Agent } from './../../../models/agent';
import { Component, OnInit, OnDestroy, ViewChild, NgZone } from '@angular/core';
import { DataTableDirective } from 'angular-datatables';
import { Router } from '@angular/router';
import * as moment from 'moment';
import { PNotifyService } from '../../../services/pnotify.service';
import { AuthService } from './../../../services/auth.service';

@Component({
  selector: 'app-agent-datatable',
  templateUrl: './agent-datatable.component.html',
  styleUrls: ['./agent-datatable.component.css']
})
export class AgentDatatableComponent implements OnInit, OnDestroy {

  filter = {
    name: '',
    companyId: 0
  };
  staticFilter = {
    name: '',
    companyId: 0
  };

  public companyData: any[];

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
  agents: any[];
  pnotify = undefined;
  constructor(
    private http: HttpClient,
    private router: Router,
    private zone: NgZone,
    private authService: AuthService,
    pnotifyService: PNotifyService
  ) {
    // this.filterForm = new FormGroup();
    if (!this.authService.hasRole('ROLE_ADMIN')) {
      this.filter.companyId = this.authService.sessionContextValue.user.company.id;
    }
    this.initCompany();
    this.staticFilter = this.filter;
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
          '/api/agent/datatables',
          dataTablesParameters,
          {}
        ).subscribe((resp: any) => {
          this.agents = resp.data;
          const dta = [];
          this.agents.forEach(agent => {
            dta.push({
              id: agent.id,
              name: agent.name,
              company: agent.companyName,
              role: agent.role,
              email: agent.email,
              phone: agent.phone,
              active: agent.active ? 'Yes' : 'No',
              actions: () => {
                let actions = `
                <a class="btn btnAction btnNavigate" data-url="/agent/view/${agent.id}">
                  <i class="fa fa-search fa-2x" aria-hidden="true"></i>
                </a>
                <a class="btn btnAction btnNavigate" data-url="/agent/edit/${agent.id}">
                  <i class="fa fa-pencil fa-2x" aria-hidden="true"></i>
                </a>
                `;
                if (this.authService.sessionContextValue.user.id !== agent.id) {
                  actions += `
                  <button class="btn btnAction btnDelete" data-agent-id="${agent.id}">
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
        $(nRow).attr('id', 'agent_' + aData.id);
      },
      columns: [
        { data: 'name' },
        { data: 'company' },
        { data: 'role' },
        { data: 'email' },
        { data: 'phone' },
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
      this.onDelete($(event.currentTarget).data('agentId'));
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
      .delete('/api/agent/' + id)
      .subscribe(
        result => {
          document.getElementById('agent_' + id).remove();
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

  initCompany() {
    if (this.authService.hasRole('ROLE_AGENT')) {
      const company = this.authService.sessionContextValue.user.company;
      this.filter.companyId = company.id;
      this.companyData = [{
        id: company.id,
        text: company.name,
        selected: true
      }];
    } else {
      this.http.get(
        '/api/company/select2'
      ).subscribe((companies: any[]) => {
        this.companyData = companies.map((company, index) => {
          return {
            id: company.id,
            text: company.name,
            // selected: company.id == this.search.companyId
          };
        });
        this.companyData.unshift({
          id: 0,
          text: '--',
          selected: true
        });
      });
    }
  }
  companyChanged(e: any) {
    this.filter.companyId = e.value;
  }

  onFilter(): void {
    this.staticFilter = this.filter;
    this.datatableElement.dtInstance.then((dtInstance: DataTables.Api) => {
      dtInstance.draw();
    });
  }

}
