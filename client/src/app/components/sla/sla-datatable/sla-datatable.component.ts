import { Observable } from 'rxjs/Observable';
import { DataTablesResponse } from './../../../models/DataTablesResponse';
import { HttpClient } from '@angular/common/http';
import { Sla } from './../../../models/sla';
import { Component, OnInit, OnDestroy, ViewChild, NgZone } from '@angular/core';
import { DataTableDirective } from 'angular-datatables';
import { Router } from '@angular/router';
import * as moment from 'moment';
import { PNotifyService } from '../../../services/pnotify.service';

@Component({
  selector: 'app-sla-datatable',
  templateUrl: './sla-datatable.component.html',
  styleUrls: ['./sla-datatable.component.css']
})
export class SlaDatatableComponent implements OnInit, OnDestroy {

  filter = {
    name: '',
    description: '',
  };
  staticFilter = {
    name: '',
    description: '',
  };

  @ViewChild(DataTableDirective, {static: false}) datatableElement: DataTableDirective;

  // ----- Start Option Scrollbar -----------
  isScrollbar: true;
  scrollbarOptions = {
    axis: 'y',
    theme: 'minimal-dark'
  };
  // ----- End Option Scrollbar -----------

  selectedAll = false;
  selectedMulti = false;

  dtOptions: any = {};
  slas: any[];
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
        dataTablesParameters.filter = this.staticFilter;
        this.http.post<DataTablesResponse>(
          '/api/sla/list',
          dataTablesParameters,
          {}
        ).subscribe((resp: any) => {
          this.slas = resp.data;
          const dta = [];
          this.slas.forEach(sla => {
            dta.push({
              name: sla.name,
              description: sla.description,
              latency: sla.latency,
              price: sla.price,
              actions: `
                <a class="btn btnAction btnNavigate" data-url="/sla/view/${sla.id}">
                  <i class="fa fa-search fa-2x" aria-hidden="true"></i>
                </a>
                <a class="btn btnAction btnNavigate" data-url="/sla/edit/${sla.id}">
                  <i class="fa fa-pencil fa-2x" aria-hidden="true"></i>
                </a>
                <button class="btn btnAction btnDelete" data-sla-id="${sla.id}">
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
        $(nRow).attr('id', 'sla_' + aData.id);
      },
      columns: [
        { data: 'name' },
        { data: 'description' },
        { data: 'latency' },
        { data: 'price' },
        { data: 'actions' },
      ],
      dom: '<t> <"row" <"col-md-4"l><"col-md-8"p>>',
      language: {
        'sProcessing': 'Traitement en cours...',
        'sSearch': 'Rechercher&nbsp;:',
        'sLengthMenu': 'Afficher _MENU_ &eacute;l&eacute;ments',
        'sInfo': 'Affichage de l\'&eacute;l&eacute;ment _START_ &agrave; _END_ sur _TOTAL_ &eacute;l&eacute;ments',
        'sInfoEmpty': 'Affichage de l\'&eacute;l&eacute;ment 0 &agrave; 0 sur 0 &eacute;l&eacute;ment',
        'sInfoFiltered': '(filtr&eacute; de _MAX_ &eacute;l&eacute;ments au total)',
        'sInfoPostFix': '',
        'sLoadingRecords': 'Chargement en cours...',
        'sZeroRecords': 'Aucun &eacute;l&eacute;ment &agrave; afficher',
        'sEmptyTable': 'Aucune donn&eacute;e disponible dans le tableau',
        'oPaginate': {
          'sFirst': 'Premier',
          'sPrevious': 'Pr&eacute;c&eacute;dent',
          'sNext': 'Suivant',
          'sLast': 'Dernier'
        },
        'oAria': {
          'sSortAscending': ': activer pour trier la colonne par ordre croissant',
          'sSortDescending': ': activer pour trier la colonne par ordre d&eacute;croissant'
        }
      },
      // Use this attribute to enable the responsive extension
      responsive: true
    };
    $('table').on('click', '.btnDelete', (event) => {
      this.onDelete($(event.currentTarget).data('slaId'));
    });
    $('table').on('click', '.btnNavigate', (event) => {
      this.onNavigate($(event.currentTarget).data('url'));
    });
    $('table').on('change', '[name="checkboxs[]"]', (event) => this.onCheckChange());
    $('table').on('draw.dt', (event) => this.onCheckChange());
  }

  ngOnDestroy(): void {
    $.fn['dataTable'].ext.search.pop();
    $('table').off();
  }

  onSelectedAllChange() {
    $('[name="checkboxs[]"]').prop('checked', this.selectedAll);
    this.onCheckChange();
  }

  onCheckChange() {
    const length = $('[name="checkboxs[]"]:checked').length;
    if (length > 1) {
      this.selectedMulti = true;
    } else {
      this.selectedMulti = false;
    }
  }

  onNavigate(url) {
    this.zone.run(() => this.router.navigateByUrl(url));
  }

  onDelete(id) {
    this.pnotify.notice({
      title: 'Confirmation',
      text: 'Voulez-vous supprimer cet element ?',
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
              text: 'Annuler',
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

  onBulkDelete() {
    this.pnotify.notice({
      title: 'Confirmation',
      text: 'Voulez-vous supprimer la selection ?',
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
                $('[name="checkboxs[]"]:checked').each((i, elt: any) => {
                  this.delete($(elt).val());
                });
                notice.close();
              }
            },
            {
              text: 'Annuler',
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
      .delete('/api/sla/' + id)
      .subscribe(
        result => {
          document.getElementById('sla_' + id).remove();
        },
        error => {
          this.pnotify.error({
            title: 'Erreur',
            text: 'Une erreur est survenue !',
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
