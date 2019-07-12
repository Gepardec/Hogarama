import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, CanLoad, Router } from '@angular/router';
import {from, Observable, Subject} from 'rxjs';
import { AuthenticationService } from 'src/app/services/AuthenticationService/authentication.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanLoad {
// tslint:disable-next-line: variable-name
  private _canLoad: boolean;

  constructor(private authService: AuthenticationService, private router: Router) {
    this._canLoad = this.authService.isCurrentlyAuthenticated();
    this.authService.isAuthenticated().subscribe((status: boolean) => {
      this._canLoad = status;
    });
  }

  canLoad(route: import ('@angular/router').Route, segments: import ('@angular/router').UrlSegment[])
    : boolean | Observable<boolean> | Promise<boolean> {

    return new Promise<boolean>((resolve, reject) => {
      return this.authService.isKeycloakAuthenticated().then((val) => {
        console.log('KEYCLOAK AUTH ' + val);
        if (!val) {
          this.router.navigateByUrl('/testLoginRedirect');
        }
        resolve(val);
      }, (val) => {
        console.log('KEYCLOAK AUTH REJ' + val);
        reject(val);
      });
    });
  }
}
