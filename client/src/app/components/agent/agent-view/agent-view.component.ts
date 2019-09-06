import { AuthService } from './../../../services/auth.service';
import { Agent } from './../../../models/agent';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { PNotifyService } from '../../../services/pnotify.service';

@Component({
  selector: 'app-agent-view',
  templateUrl: './agent-view.component.html',
  styleUrls: ['./agent-view.component.css']
})
export class AgentViewComponent implements OnInit {

  agent: Agent;
  isAccountPage: boolean;
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
    this.agent = new Agent();
    this.isAccountPage = (this.router.url.indexOf('/account') === 0);
    this.route.params.subscribe(params => {
      const id = this.isAccountPage ?
        authService.sessionContextValue.user.id : params['id'];
      this.http.get(
        '/api/agent/' + id
      ).subscribe((agent: Agent) => {
        this.agent = agent;
      });
    });
    this.pnotify = pnotifyService.getPNotify();
  }

  ngOnInit() {
  }

  onSubmit(): void {
  }

}
