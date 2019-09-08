import { AuthService } from './../../../services/auth.service';
import { User } from './../../../models/user';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { PNotifyService } from '../../../services/pnotify.service';

@Component({
  selector: 'app-admin-add',
  templateUrl: './admin-add.component.html',
  styleUrls: ['./admin-add.component.css']
})
export class AdminAddComponent implements OnInit {

  admin: User;
  isAccountPage: boolean;
  reservedUsernames: string[];
  roles: {};
  public rolesData: Array<any>;
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
    this.admin = new User();
    this.admin.role = 'ADMIN';
    this.reservedUsernames = [];
    this.http.get(
      '/api/admin/reservedUsernames/0'
    ).subscribe((usernames: any[]) => {
      this.reservedUsernames = usernames;
    });
    this.roles = {
      ADMIN: 'ADMIN',
      SUPER_ADMIN: 'ADMIN',
    };
    this.rolesData = [
      {
        id: 'ADMIN',
        text: 'ADMIN',
        selected: this.admin.role == 'ADMIN' ? true : false
      },
      {
        id: 'SUPER_ADMIN',
        text: 'SUPER_ADMIN',
        selected: this.admin.role == 'SUPER_ADMIN' ? true : false
      },
    ];
    this.pnotify = pnotifyService.getPNotify();
  }

  ngOnInit() {
  }

  roleChanged(e: any) {
    this.admin.role = this.roles[e.value];
  }

  onUsernameChange() {
    this.admin.username = this.admin.username.replace(/\s/g, '');
  }

  onSubmit(): void {
    this.http.post(
      '/api/admin',
      this.admin
    ).subscribe(
      (admin: User) => {
        this.admin = admin;
        this.router.navigate(['/admin/view', this.admin.id]);
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
