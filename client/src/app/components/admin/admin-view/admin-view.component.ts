import { AuthService } from './../../../services/auth.service';
import { Admin } from './../../../models/admin';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { PNotifyService } from '../../../services/pnotify.service';

@Component({
  selector: 'app-admin-view',
  templateUrl: './admin-view.component.html',
  styleUrls: ['./admin-view.component.css']
})
export class AdminViewComponent implements OnInit {

  admin: Admin;
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
    this.admin = new Admin();
    this.isAccountPage = (this.router.url.indexOf('/account') === 0);
    this.route.params.subscribe(params => {
      const id = this.isAccountPage ?
        authService.sessionContextValue.user.id : params['id'];
      this.http.get(
        '/api/admin/' + id
      ).subscribe((admin: Admin) => {
        this.admin = admin;
      });
    });
    this.pnotify = pnotifyService.getPNotify();
  }

  ngOnInit() {
  }

  onSubmit(): void {
  }

}
