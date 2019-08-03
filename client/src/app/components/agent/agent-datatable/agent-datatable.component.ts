import { Component, OnInit } from '@angular/core';
import { PNotifyService } from './../../../services/pnotify.service';

@Component({
  selector: 'app-agent-datatable',
  templateUrl: './agent-datatable.component.html',
  styleUrls: ['./agent-datatable.component.css']
})
export class AgentDatatableComponent implements OnInit {

  dtOptions: DataTables.Settings = {};
  selectedAll = false;
  selectedMulti = false;

  pnotify = undefined;
  constructor(pnotifyService: PNotifyService) {
    this.pnotify = pnotifyService.getPNotify();
  }

  ngOnInit() {

    this.dtOptions = {
      searching: false,
      pageLength: 5,
      processing: true,
      lengthMenu: [[5, 10, 25, 50, 100], [5, 10, 25, 50, 100]],
      order: [[1, 'asc']],
      ordering: false,
      columnDefs: [
        { orderable: false, targets: 0 }
      ],
      dom: '<t> <"row" <"col-md-4"l><"col-md-8"p>>',
      language: {
        processing: 'Traitement en cours...',
        search: 'Rechercher&nbsp;:',
        lengthMenu: 'Afficher _MENU_ &eacute;l&eacute;ments',
        info: 'Affichage de l\'&eacute;l&eacute;ment _START_ &agrave; _END_ sur _TOTAL_ &eacute;l&eacute;ments',
        infoEmpty: 'Affichage de l\'&eacute;l&eacute;ment 0 &agrave; 0 sur 0 &eacute;l&eacute;ment',
        infoFiltered: '(filtr&eacute; de _MAX_ &eacute;l&eacute;ments au total)',
        infoPostFix: '',
        loadingRecords: 'Chargement en cours...',
        zeroRecords: 'Aucun &eacute;l&eacute;ment &agrave; afficher',
        emptyTable: 'Aucune donn&eacute;e disponible dans le tableau',
        paginate: {
          first: 'Premier',
          previous: 'Pr&eacute;c&eacute;dent',
          next: 'Suivant',
          last: 'Dernier'
        },
        aria: {
          sortAscending: ': activer pour trier la colonne par ordre croissant',
          sortDescending: ': activer pour trier la colonne par ordre d&eacute;croissant'
        }
      },
      // Use this attribute to enable the responsive extension
      responsive: true
    };

  }

  onSelectedAllChange() {
    $('[name="checkboxs[]"]:enabled').prop('checked', this.selectedAll);
    this.onCheckChange();
  }
  onCheckChange() {
    const length = $('[name="checkboxs[]"]:checked').length;
    if (length > 1) {
      this.selectedMulti = true;
      this.selectedAll = true;
    } else {
      this.selectedMulti = false;
      this.selectedAll = false;
    }
  }

  onDeleteAll() {
    this.pnotify.notice({
      title: 'Confirmation',
      text: 'Voulez-vous supprimer tout les éléments ?',
      stack: {
        dir1: 'down',
        firstpos1: 25,
        modal: true,
        overlay_close: true
      },
      hide: false,
      modules: {
        Confirm: {
          confirm: true,
          focus: false,
          buttons: [
            {
              text: 'Ok',
              addClass: 'btn btn-chico',
              click: notice => {
                alert('OK');
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
  onDelete() {
    this.pnotify.notice({
      title: 'Confirmation',
      text: 'Voulez-vous supprimer cet element ?',
      stack: {
        dir1: 'down',
        firstpos1: 25,
        modal: true,
        overlay_close: true
      },
      hide: false,
      modules: {
        Confirm: {
          confirm: true,
          focus: false,
          buttons: [
            {
              text: 'Ok',
              addClass: 'btn btn-chico',
              click: notice => {
                alert('OK');
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


}
