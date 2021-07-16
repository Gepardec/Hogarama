import {Injectable} from '@angular/core';
import {CanLoad, Router} from '@angular/router';
import {Observable} from 'rxjs';
import {AuthenticationService} from 'src/app/services/AuthenticationService/authentication.service';

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
      const authed = this.authService.isKeycloakAuthenticated();
      console.log('KEYCLOAK AUTH ' + authed);
      if (!authed) {
        this.router.navigateByUrl('/login');
      }
      resolve(authed);
    });
  }
}
