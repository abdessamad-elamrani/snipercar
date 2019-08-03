import { Component, OnInit } from '@angular/core';
import { PNotifyService } from './../../services/pnotify.service';
import { Select2OptionData } from 'ng2-select2';
import * as moment from 'moment';
@Component({
  selector: 'app-extra',
  templateUrl: './extra.component.html',
  styleUrls: ['./extra.component.css']
})
export class ExtraComponent implements OnInit {
  public exampleData: Array<Select2OptionData>;
  public exampleOption: any;
  pnotify = undefined;
  // ----- Start Ion Range Slider -----------
  ionMin = moment('0000', 'hmm').format('X');
  ionMax = moment('2359', 'hmm').format('X');
  ionFrom = moment('0000', 'hmm').format('X');
  ionTo = moment('2359', 'hmm').format('X');
  hourFormat = function (num) {
    const m = moment(num, 'X');
    return m.format('HH:mm');
  };
  formatX = function (time: string) {
    return moment(time, 'h:mm').format('X');
  };
  // ----- Start Ion Range Slider -----------

  constructor(pnotifyService: PNotifyService) {
    this.pnotify = pnotifyService.getPNotify();
  }


  ngOnInit() {
    this.exampleData = [
      {
        id: 'basic1',
        text: 'Basic 1'
      },
      {
        id: 'basic2',
        disabled: true,
        text: 'Basic 2'
      },
      {
        id: 'basic3',
        text: 'Basic 3'
      },
      {
        id: 'basic4',
        text: 'Basic 4'
      }
    ];
    this.exampleOption = {
      multiple: 'true'
    };
  }


  onCorfim() {
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
                alert('test');
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
  onError() {
    this.pnotify.error({
      title: 'Erreur',
      text: 'Une erreur est survenue !',
      stack: {
        dir1: 'down',
        firstpos1: 25,
        // firstpos1: 70, firstpos2: 10,
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
                notice.close();
              }
            }
          ]
        }
      }
    });
  }
}
