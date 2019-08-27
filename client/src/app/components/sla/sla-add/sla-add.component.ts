import { Sla } from './../../../models/sla';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { PNotifyService } from '../../../services/pnotify.service';
// declare var jQuery: any;

@Component({
  selector: 'app-sla-add',
  templateUrl: './sla-add.component.html',
  styleUrls: ['./sla-add.component.css']
})
export class SlaAddComponent implements OnInit {

  sla: Sla;
  pnotify = undefined;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router,
    pnotifyService: PNotifyService
  ) {
    this.sla = new Sla();
    this.pnotify = pnotifyService.getPNotify();
  }

  ngOnInit() {
  }

  onSubmit(): void {
    this.http.post(
      '/api/sla',
      this.sla
    ).subscribe(
      (sla: Sla) => {
        this.sla = sla;
        this.router.navigate(['/sla/view', this.sla.id]);
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
