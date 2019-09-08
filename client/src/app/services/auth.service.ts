import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { NgxPermissionsService, NgxRolesService } from 'ngx-permissions';

import { environment } from '../../environments/environment';
import { SessionContext } from '../models/session-context';

@Injectable({ providedIn: 'root' })
export class AuthService {
    private sessionContextSubject: BehaviorSubject<SessionContext>;
    public sessionContext: Observable<SessionContext>;

    constructor(
      private http: HttpClient,
      private roleService: NgxRolesService,
      private permissionService: NgxPermissionsService
    ) {
        this.sessionContextSubject = new BehaviorSubject<SessionContext>(JSON.parse(localStorage.getItem('sessionContext')));
        this.sessionContext = this.sessionContextSubject.asObservable();
    }

    public get sessionContextValue(): SessionContext {
        return this.sessionContextSubject.value;
    }

    login(username: string, password: string) {
        return this.http.post<any>(`/api/auth/token/generate`, { username, password })
            .pipe(map(sessionContext => {
                // store sessionContext details and jwt token in local storage to keep user logged in between page refreshes
                localStorage.setItem('sessionContext', JSON.stringify(sessionContext));
                this.sessionContextSubject.next(sessionContext);
                this.loadRoles();
                return sessionContext;
            }));
    }

    loadRoles() {
      this.roleService.flushRoles();
      this.permissionService.flushPermissions();
      this.permissionService.loadPermissions([]);
      if (this.isLoggedIn) {
        this.sessionContextValue.roles.forEach((role, idx) => {
          this.roleService.addRole(role, []);
        });
      }
    }

    logout() {
      // remove sessionContext from local storage to log user out
      localStorage.removeItem('sessionContext');
      this.sessionContextSubject.next(null);
      this.roleService.flushRoles();
    }

    public get isLoggedIn() {
      const sessionContext = this.sessionContextValue;
      if (sessionContext) {
          // logged in so return true
          return true;
      }
      return false;
    }

    public hasRole(role: string) {
      return this.roleService.getRole(role) ? true : false;
    }
}
