import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {environment} from "../../../environments/environment";
import * as Keycloak from "keycloak-js";
import {PlatformInfoService} from "../PlatformInfoService/platform-info.service";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
    private _isAuthenticated: BehaviorSubject<boolean> = new BehaviorSubject(false);
    private _keycloak;

    constructor(
        private platformInfo: PlatformInfoService
    ) {
        this._keycloak = Keycloak(environment.keycloak);
        this._isAuthenticated.next(this.isKeycloakAuthenticated());
    }

    public init(): Promise<boolean> {
        return this._keycloak.init({
            adapter: this.platformInfo.isCurrentPlatformApp() ? 'cordova' : 'default',
            promiseType: 'native'
        }).then((isAuthed) => {
            console.log('Authentication Status: ', isAuthed);
            this._isAuthenticated.next(isAuthed);

            return isAuthed;
        }).catch((error) => {
            console.log('Error in init: ', error);
            this._isAuthenticated.next(false);

            return false;
        });
    }

    public getRoles(): string[] {
        return this._keycloak.realmAccess.roles;
    }

    public loginUser(): Promise<boolean> {
        return this._keycloak.login().then(() => {
            this._isAuthenticated.next(true);
            return true;
        });
    }


    public logoutUser(): Promise<void> {
        return this._keycloak.logout().then(() => {
            this._isAuthenticated.next(false);
            return;
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