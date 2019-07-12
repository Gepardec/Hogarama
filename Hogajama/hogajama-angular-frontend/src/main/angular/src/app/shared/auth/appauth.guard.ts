import { Injectable } from '@angular/core';
import {CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot, CanLoad} from '@angular/router';
import { KeycloakService, KeycloakAuthGuard } from 'keycloak-angular';
import {Observable} from 'rxjs';

@Injectable()
export class AppAuthGuard extends KeycloakAuthGuard implements CanLoad {
  constructor(protected router: Router, protected keycloakAngular: KeycloakService) {
    super(router, keycloakAngular);
  }

  isAccessAllowed(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<boolean> {
    return new Promise((resolve, reject) => {
      if (!this.authenticated) {
        this.keycloakAngular.login();
        return;
      }

      const requiredRoles = route.data.roles;
      if (!requiredRoles || requiredRoles.length === 0) {
        return resolve(true);
      } else {
        if (!this.roles || this.roles.length === 0) {
          resolve(false);
        }
        let granted = false;
        for (const requiredRole of requiredRoles) {
          if (this.roles.indexOf(requiredRole) > -1) {
            granted = true;
            break;
          }
        }
        resolve(granted);
      }
    });
  }

  canLoad(route: import ('@angular/router').Route, segments: import ('@angular/router').UrlSegment[])
    : boolean | Observable<boolean> | Promise<boolean> {

    return false;
  }
}
