import { AuthService } from './../../../services/auth.service';
import { User } from './../../../models/user';
import { Selection } from './../../../models/selection';
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

  agent: User;
  selections: {};
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
      this.agent.smsNotif = dashboard.hasOwnProperty('smsNotif') ? dashboard.smsNotif : false;
      this.agent.emailNotif = dashboard.hasOwnProperty('emailNotif') ? dashboard.emailNotif : false;
      this.selections = {};
      if (dashboard.hasOwnProperty('selections')) {
        dashboard.selections.forEach((selection, index) => {
          this.selections[selection.id] = selection;
        });
      }
      this.agent.currectSelection = dashboard.currectSelection;
    });
    this.pnotify = pnotifyService.getPNotify();
  }

  ngOnInit() {
  }

  smsToggle() {
    this.updateDashboard(!this.agent.smsNotif, this.agent.emailNotif, this.agent.currectSelection ? this.agent.currectSelection.id : 0);
  }

  emailToggle() {
    this.updateDashboard(this.agent.smsNotif, !this.agent.emailNotif, this.agent.currectSelection ? this.agent.currectSelection.id : 0);
  }

  onSelection(id) {
    this.agent.currectSelection = this.selections[id];
    this.updateDashboard(this.agent.smsNotif, this.agent.emailNotif, this.agent.currectSelection ? this.agent.currectSelection.id : 0);
  }

  updateDashboard(smsNotif, emailNotif, currentSelectionId) {
    this.http.put(
    '/api/agent/dashboard/' + this.agent.id + '/' + smsNotif + '/' + emailNotif + '/' + currentSelectionId,
      {}
    ).subscribe(
      (dashboard: any) => {
        this.agent.smsNotif = dashboard.hasOwnProperty('smsNotif') ? dashboard.smsNotif : false;
        this.agent.emailNotif = dashboard.hasOwnProperty('emailNotif') ? dashboard.emailNotif : false;
        this.selections = {};
        if (dashboard.hasOwnProperty('selections')) {
          dashboard.selections.forEach((selection, index) => {
            this.selections[selection.id] = selection;
          });
        }
        this.agent.currectSelection = dashboard.currectSelection;
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
