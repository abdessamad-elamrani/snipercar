import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor() { }

  ngOnInit() {
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
}
