import { Component, OnInit, Input } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { SessionContext } from '../../models/session-context';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  @Input() hasFilter: boolean;
  sessionContext: SessionContext;

  constructor(
    private router: Router,
    private authService: AuthService
  ) {
    this.authService.sessionContext.subscribe(x => this.sessionContext = x);
  }

  ngOnInit() {
  }

  fliterClicked(event: MouseEvent) {
    const $filterFixed = $('.filterFixed');
    const $fliterTtoggle = $('#fliter_toggle');
    if ($filterFixed.hasClass('open')) {
      $filterFixed.removeClass('open');
    } else {
      $filterFixed.addClass('open');
    }
  }

  onResize(event) {
    const innerWidth = event.target.innerWidth;
    const body = $('body');
    const menu = $('#sidebar-menu');
    if (innerWidth >= 992) {
      menu.find('li.active-sm ul').show();
      menu.find('li.active-sm').addClass('active').removeClass('active-sm');
      menu.addClass('left_col_lg').removeClass('left_col_sm');
      body.addClass('nav-md').removeClass('nav-sm');
    }
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
