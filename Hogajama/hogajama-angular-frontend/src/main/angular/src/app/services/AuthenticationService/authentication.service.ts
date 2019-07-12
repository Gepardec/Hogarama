import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';
import {Router} from '@angular/router';
import {KeycloakService} from 'keycloak-angular';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
// tslint:disable-next-line: variable-name
  private _isAuthenticated: Subject<boolean> = new Subject();
  // tslint:disable-next-line: variable-name
  private _isCurrentlyAuthenticated: boolean;

  constructor(protected keycloakAngular: KeycloakService) {
    this.isAuthenticated().subscribe((status: boolean) => {
      this._isCurrentlyAuthenticated = status;
    });
  }

  public loginUser(): Promise<boolean> {
    return new Promise<boolean>((resolve, reject) => {
      this.keycloakAngular.login().then(() => {
        this._isAuthenticated.next(true);
        this._isCurrentlyAuthenticated = true;
        resolve(true);
      }, () => {
        this._isAuthenticated.next(false);
      });
    });
    /*return new Promise<boolean>((resolve, reject) => {
      this._isAuthenticated.next(true);
      resolve(true);
    });´*/
  }

  public logoutUser(): Promise<boolean> {
    return new Promise<boolean>((resolve, reject) => {
      this.keycloakAngular.logout().then(() => {
        this._isAuthenticated.next(false);
        this._isCurrentlyAuthenticated = false;
        resolve(false);
      }, () => {
        reject();
      });
    });
    /*return new Promise<boolean>((resolve, reject) => {
      this._isAuthenticated.next(false);
      resolve(false);
    });*/
  }

  public isAuthenticated(): Observable<boolean> {
    return this._isAuthenticated.asObservable();
  }

  public isCurrentlyAuthenticated(): boolean {
    return this._isCurrentlyAuthenticated;
  }

  public isKeycloakAuthenticated(): Promise<boolean> {
      return this.keycloakAngular.isLoggedIn();
  }
}
