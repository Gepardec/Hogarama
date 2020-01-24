import {Injectable} from '@angular/core';
import {BehaviorSubject, interval, Observable} from 'rxjs';
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

        this.isAuthenticated().subscribe((authed: boolean) => {
            if(authed) {
                interval(5000).subscribe(async () => {
                    try {
                        const updateSuccess = await this._keycloak.updateToken(environment.keycloakTokenMinValidity);

                        console.log(updateSuccess);
                    } catch (e) {
                        console.error('Token update failed', e);
                    }
                });
            }
        });
    }

    public async init(): Promise<boolean> {
        const token = localStorage.getItem('kc_token');
        const refreshToken = localStorage.getItem('kc_refreshToken');

        console.log(`Init Keycloak with token [${token}]`);
        console.log(`Init Keycloak with refresh-token [${refreshToken}]`);

        return this._keycloak.init({
            adapter: this.platformInfo.isCurrentPlatformApp() ? 'cordova' : 'default',
            promiseType: 'native',
            onLoad: 'check-sso',
            token, refreshToken
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

    public getToken(): string {
        return this._keycloak.token;
    }

    public getRefreshToken(): string {
        return this._keycloak.refreshToken;
    }

    public getRoles(): string[] {
        return this._keycloak.realmAccess.roles;
    }

    public loginUser(): Promise<boolean> {
        return this._keycloak.login().then(() => {
            localStorage.setItem('kc_token', this.getToken());
            localStorage.setItem('kc_refreshToken', this.getRefreshToken());

            this._isAuthenticated.next(true);
            return true;
        }).catch(() => {
            this._isAuthenticated.next(false);
            return false;
        });
    }

    public logoutUser(): Promise<void> {
        localStorage.setItem('kc_token', null);
        localStorage.setItem('kc_refreshToken', null);

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