import { AuthService } from './../../../services/auth.service';
import { Selection } from './../../../models/selection';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { PNotifyService } from '../../../services/pnotify.service';
// declare var jQuery: any;

@Component({
  selector: 'app-selection-add',
  templateUrl: './selection-add.component.html',
  styleUrls: ['./selection-add.component.css']
})
export class SelectionAddComponent implements OnInit {

  selection: Selection;
  pnotify = undefined;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router,
    private authService: AuthService,
    pnotifyService: PNotifyService
  ) {
    this.selection = new Selection();
    this.selection.user = this.authService.sessionContextValue.user;
    this.pnotify = pnotifyService.getPNotify();
  }

  ngOnInit() {
  }

  onSubmit(): void {
    this.http.post(
      '/api/selection',
      this.selection
    ).subscribe(
      (selection: Selection) => {
        this.selection = selection;
        this.router.navigate(['/selection/view', this.selection.id]);
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
