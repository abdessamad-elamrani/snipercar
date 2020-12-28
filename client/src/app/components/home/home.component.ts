import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { NgxRolesService } from 'ngx-permissions';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(
    private router: Router,
    private authService: AuthService,
    private roleService: NgxRolesService
  ) { }

  ngOnInit() {
    //commented by abde
    /*  
    if (this.roleService.getRole('ROLE_ADMIN')) {
      this.router.navigate(['/agent/list']);
    } else if (this.roleService.getRole('ROLE_AGENT')) {
      this.router.navigate(['/agent/dashboard']);
    // } else {
    //   this.router.navigate(['/logout']);
    }
    */
  }

}
