import { AuthService } from './../../../services/auth.service';
import { User } from './../../../models/user';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { PNotifyService } from '../../../services/pnotify.service';

@Component({
  selector: 'app-agent-edit',
  templateUrl: './agent-edit.component.html',
  styleUrls: ['./agent-edit.component.css']
})
export class AgentEditComponent implements OnInit {

  user: any;
  agent: User;
  isAccountPage: boolean;
  reservedUsernames: string[];
  public rolesData: any[];
  public companiesData: any[];
  companies: {};
  pnotify = undefined;
  // ----- Start DatePicker -----------
  dateTimeFilter = (d: Date): boolean => {
    return true;
  }
  // ----- End DatePicker -----------

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router,
    private authService: AuthService,
    pnotifyService: PNotifyService
  ) {
    this.user = authService.sessionContextValue.user;
    this.agent = new User();
    this.reservedUsernames = [];
    this.isAccountPage = (this.router.url.indexOf('/account') === 0);
    this.route.params.subscribe(params => {
      const id = this.isAccountPage ? this.user.id : params['id'];
      this.http.get(
        '/api/agent/' + id
      ).subscribe((agent: User) => {
        this.agent = agent;
        this.http.get(
          '/api/agent/reservedUsernames/' + id
        ).subscribe((usernames: any[]) => {
          this.reservedUsernames = usernames;
        });
        this.http.get(
          '/api/company'
        ).subscribe((companies: any[]) => {
          this.companies = {};
          this.companiesData = [];
          companies.forEach((company, index) => {
            this.companies[company.id] = company;
            this.companiesData.push({
              id: company.id,
              text: company.name,
              selected: this.agent.company && this.agent.company.id == company.id ? true : false
            });
          });
        });
        this.rolesData = [
          {
            id: 'AGENT',
            text: 'AGENT',
            selected: this.agent.role == 'AGENT' ? true : false
          },
          {
            id: 'SUPER_AGENT',
            text: 'SUPER_AGENT',
            selected: this.agent.role == 'SUPER_AGENT' ? true : false
          },
        ];
      });
    });
    this.pnotify = pnotifyService.getPNotify();
  }

  ngOnInit() {
  }

  hasRole(role: string) {
    return this.authService.hasRole(role);
  }

  roleChanged(e: any) {
    this.agent.role = e.value;
  }

  companyChanged(e: any) {
    this.agent.company = this.companies[e.value];
  }

  onUsernameChange() {
    this.agent.username = this.agent.username.replace(' ', '');
  }

  onSubmit(): void {
    this.http.put(
      '/api/agent/' + this.agent.id,
      this.agent
    ).subscribe(
      (agent: User) => {
        this.agent = agent;
        this.router.navigate(this.isAccountPage ? ['/account/agent/view'] : ['/agent/view', this.agent.id]);
      },
      (error) => {
        this.pnotify.error({
          title: 'Error',
          text: 'An Error has occured',
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
