import { AuthService } from './../../../services/auth.service';
import { Sla } from './../../../models/sla';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { PNotifyService } from '../../../services/pnotify.service';

@Component({
  selector: 'app-sla-edit',
  templateUrl: './sla-edit.component.html',
  styleUrls: ['./sla-edit.component.css']
})
export class SlaEditComponent implements OnInit {

  sla: Sla;
  pnotify = undefined;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router,
    private authService: AuthService,
    pnotifyService: PNotifyService
  ) {
    this.sla = new Sla();
    this.route.params.subscribe(params => {
      this.http.get(
        '/api/sla/' + params['id']
      ).subscribe((sla: Sla) => {
        this.sla = sla;
      });
    });
    this.pnotify = pnotifyService.getPNotify();
  }

  ngOnInit() {
  }

  onSubmit(): void {
    this.http.put(
      '/api/sla/' + this.sla.id,
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
