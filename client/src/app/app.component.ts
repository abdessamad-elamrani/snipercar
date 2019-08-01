import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  public scrollbarOptions = {
    axis: 'y',
    theme: 'inset-dark',
    // mouseWheel: { enable: false }
  };
  constructor(private router: Router) {

  }

  get isLoggedIn() {
    if (
      this.router.url.indexOf('/login') === -1
      && this.router.url.indexOf('/home') === -1
    ) {
      return true;
    } else {
      return false;
    }

  }
}
