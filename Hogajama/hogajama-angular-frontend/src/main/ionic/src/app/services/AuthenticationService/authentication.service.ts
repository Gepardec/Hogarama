import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {environment} from "../../../environments/environment";
import * as Keycloak from "keycloak-js";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  // tslint:disable-next-line: variable-name
    private _isAuthenticated: BehaviorSubject<boolean> = new BehaviorSubject(false);
    private _keycloak;

    constructor() {
        this._keycloak = Keycloak(environment.keycloak);
        this._isAuthenticated.next(this.isKeycloakAuthenticated());
    }

    public init(): Promise<boolean> {
        return this._keycloak.init({
            onLoad: 'check-sso',
            adapter: 'default',
            promiseType: 'native'
        }).then((isAuthed) => {
            this._isAuthenticated.next(isAuthed);
        });
    }

    public getRoles(): string[] {
        return this._keycloak.realmAccess.roles;
    }

    public loginUser(): Promise<boolean> {
      return this._keycloak.login().then(() => {
          this._isAuthenticated.next(true);
      });
    }


    public logoutUser(): Promise<boolean> {
      return this._keycloak.logout().then(() => {
          this._isAuthenticated.next(false);
        });
    }

    public isAuthenticated(): Observable<boolean> {
      return this._isAuthenticated.asObservable();
    }

    public isCurrentlyAuthenticated(): boolean {
      return this._isAuthenticated.getValue();
    }

    public isKeycloakAuthenticated(): boolean {
        return this._keycloak.authenticated;
    }
}
