import { AuthService } from './../../../services/auth.service';
import { User } from './../../../models/user';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { PNotifyService } from '../../../services/pnotify.service';

@Component({
  selector: 'app-admin-edit',
  templateUrl: './admin-edit.component.html',
  styleUrls: ['./admin-edit.component.css']
})
export class AdminEditComponent implements OnInit {

  user: any;
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
    this.user = authService.sessionContextValue.user;
    this.admin = new User();
    this.reservedUsernames = [];
    this.isAccountPage = (this.router.url.indexOf('/account') === 0);
    this.roles = {
      ADMIN: 'ADMIN',
      SUPER_ADMIN: 'ADMIN',
    };
    this.route.params.subscribe(params => {
      const id = this.isAccountPage ? this.user.id : params['id'];
      this.http.get(
        '/api/admin/' + id
      ).subscribe((admin: User) => {
        this.admin = admin;
        console.error('this.admin', this.admin);
        this.http.get(
          '/api/admin/reservedUsernames/' + id
        ).subscribe((usernames: any[]) => {
          for (let i = 0; i < usernames.length; i++) {
            this.reservedUsernames.push(usernames[i].username);
          }
        });
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
      });
    });
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
    this.http.put(
      '/api/admin/' + this.admin.id,
      this.admin
    ).subscribe(
      (admin: User) => {
        this.admin = admin;
        this.router.navigate(this.isAccountPage ? ['/account/admin/view'] : ['/admin/view', this.admin.id]);
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
