import { Injectable } from '@angular/core';
import { Subject, Observable, BehaviorSubject } from 'rxjs';
import {KeycloakService} from 'keycloak-angular';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  // tslint:disable-next-line: variable-name
    private _isAuthenticated: BehaviorSubject<boolean> = new BehaviorSubject(false);

    constructor(protected keycloakAngular: KeycloakService) {
      this.isKeycloakAuthenticated().then(isAuthed => {
        this._isAuthenticated.next(isAuthed);
      });
    }

    public loginUser(): Promise<boolean> {
      return new Promise<boolean>((resolve, reject) => {
        this.keycloakAngular.login().then(() => {
          this._isAuthenticated.next(true);
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
          resolve(false);
        }, () => {
          reject();
        });
      });
    }

    public isAuthenticated(): Observable<boolean> {
      return this._isAuthenticated.asObservable();
    }

    public isCurrentlyAuthenticated(): boolean {
      return this._isAuthenticated.getValue();
    }

    public isKeycloakAuthenticated(): Promise<boolean> {
        return this.keycloakAngular.isLoggedIn();
    }
}
