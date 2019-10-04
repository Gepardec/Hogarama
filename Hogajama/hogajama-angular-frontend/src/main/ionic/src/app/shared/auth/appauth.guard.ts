import {Injectable} from '@angular/core';
import {CanLoad, Router} from '@angular/router';
import {Observable} from 'rxjs';
import {AuthenticationService} from "../../services/AuthenticationService/authentication.service";

@Injectable()
export class AppAuthGuard implements CanLoad {
  constructor(protected router: Router, protected keycloak: AuthenticationService) {
  }

  canLoad(route: import ('@angular/router').Route, segments: import ('@angular/router').UrlSegment[])
    : boolean | Observable<boolean> | Promise<boolean> {
    return new Promise((resolve, reject) => {
      if (!this.keycloak.isCurrentlyAuthenticated()) {
        this.keycloak.loginUser();
        return;
      }

      const requiredRoles = route.data.roles;
      if (!requiredRoles || requiredRoles.length === 0) {
        return resolve(true);
      } else {
        if (!this.keycloak.getRoles() || this.keycloak.getRoles().length === 0) {
          resolve(false);
        }
        let granted = false;
        for (const requiredRole of requiredRoles) {
          if (this.keycloak.getRoles().indexOf(requiredRole) > -1) {
            granted = true;
            break;
          }
        }
        resolve(granted);
      }
    });
  }
}
