import { AuthService } from './services/auth.service';
import { SessionContext } from './models/session-context';
import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit  {
  public scrollbarOptions = {
    axis: 'y',
    theme: 'inset-dark',
    // mouseWheel: { enable: false }
  };
  sessionContext: SessionContext;
  hasFilter = false;

  constructor(
    private router: Router,
    private authService: AuthService
  ) {
    this.authService.sessionContext.subscribe(x => this.sessionContext = x);
    this.router.events.subscribe(value => {
      if (this.router.url.indexOf('/list') === -1) {
        this.hasFilter = false;
      } else {
        this.hasFilter = true;
      }
  });
  }

  ngOnInit() {
    this.authService.loadRoles();
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  get isLoggedIn() {
    return this.authService.isLoggedIn === true;
  }
}
