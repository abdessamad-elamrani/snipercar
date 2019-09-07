import { AuthService } from './../../../services/auth.service';
import { User } from './../../../models/user';
import { Selection } from './../../../models/selection';
import { Company } from './../../../models/company';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { PNotifyService } from '../../../services/pnotify.service';

@Component({
  selector: 'app-agent-dashboard',
  templateUrl: './agent-dashboard.component.html',
  styleUrls: ['./agent-dashboard.component.css']
})
export class AgentDashboardComponent implements OnInit {
  public scrollbarOptions = { axis: 'y', theme: 'inset-dark' };

  agent: User;
  selections: Selection[];
  pnotify = undefined;
  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router,
    private authService: AuthService,
    pnotifyService: PNotifyService
  ) {
    this.agent = authService.sessionContextValue.user;
    this.http.get(
      '/api/agent/dashboard/' + this.agent.id
    ).subscribe((dashboard: any) => {
      this.agent.smsNotif = dashboard.smsNotif;
      this.agent.emailNotif = dashboard.emailNotif;
    });

    this.selections = [
      {
        id: 1,
        name: 'selection 1',
        user: {
          id: 1,
          name: 'user 1',
          username: 'username 1',
          email: 'email@gmail.com',
          salt: 'salt 1',
          password: '54d5g43gfd',
          passwordChange: true,
          newPassword: '1254',
          newPasswordConfirm: '1254',
          company: new Company(),
          phone: 'test',
          smsNotif: true,
          emailNotif: false,
          selections: [new Selection()],
          currectSelection: new Selection(),
          role: 'Admin',
          active: true,
        },
        filters: [
          {
            id: 1,
            url: 'Url ',
            name: 'filter 1 ',
            website: {
              id: 1,
              name: 'web 1',
              url: 'url 1'
            }
          },
          {
            id: 2,
            url: 'Url ',
            name: 'filter 2',
            website: {
              id: 2,
              name: 'web 3',
              url: 'url 1'
            },
          }
        ]
      },
      {
        id: 2,
        name: 'selection 2',
        user: {
          id: 1,
          name: 'user 1',
          username: 'username 1',
          email: 'email@gmail.com',
          salt: 'salt 1',
          password: '54d5g43gfd',
          passwordChange: true,
          newPassword: '1254',
          newPasswordConfirm: '1254',
          company: new Company(),
          phone: 'test',
          smsNotif: true,
          emailNotif: false,
          selections: [new Selection()],
          currectSelection: new Selection(),
          role: 'Admin',
          active: true,
        },
        filters: [
          {
            id: 1,
            url: 'Url ',
            name: 'filter 1 ',
            website: {
              id: 1,
              name: 'web 1',
              url: 'url 1'
            }
          },
          {
            id: 2,
            url: 'Url ',
            name: 'filter 2',
            website: {
              id: 2,
              name: 'web 3',
              url: 'url 1'
            },
          }
        ]
      },
    ];
    this.http.get(
      '' // API URL HERE
    ).subscribe((selections: any) => {
      this.selections = selections;
    });
    this.pnotify = pnotifyService.getPNotify();
  }

  ngOnInit() {
  }

  smsToggle() {
    this.updateDashboard(!this.agent.smsNotif, this.agent.emailNotif);
  }

  emailToggle() {
    this.updateDashboard(this.agent.smsNotif, !this.agent.emailNotif);
  }

  updateDashboard(smsNotif, emailNotif) {
    this.http.put(
      '/api/agent/dashboard/' + this.agent.id + '/' + smsNotif + '/' + emailNotif,
      {}
    ).subscribe(
      (dashboard: any) => {
        this.agent.smsNotif = dashboard.smsNotif;
        this.agent.emailNotif = dashboard.emailNotif;
      },
      (error) => {
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

  RowSelected(ev: any, id) {
    $('.selection').removeClass('over');
    // const id = $(ev.target).closest('.card').data('id');
    this.http.put(
      '' + id,  // API URL HERE
      {}
    ).subscribe(
      (selection: any) => {
        $(ev.target).closest('.selection').addClass('over');
      },
      (error) => {
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
}
