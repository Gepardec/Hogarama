import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {environment} from "../../../environments/environment";
import * as Keycloak from "keycloak-js";
import {PlatformInfoService} from "../PlatformInfoService/platform-info.service";
import {InAppBrowser} from "@ionic-native/in-app-browser/ngx";

import uuidv4 from "uuid/v4";
import {InAppBrowserOptions} from "@ionic-native/in-app-browser";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Storage} from "@ionic/storage";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
    // tslint:disable-next-line: variable-name
    private _isAuthenticated: BehaviorSubject<boolean> = new BehaviorSubject(false);
    private _keycloak;

    constructor(
        private platformInfo: PlatformInfoService,
        private inAppBrowser: InAppBrowser,
        private storage: Storage,
        private http: HttpClient
    ) {
        this._keycloak = Keycloak(environment.keycloak);
        this._isAuthenticated.next(this.isKeycloakAuthenticated());
    }

    public init(): Promise<boolean> {
        return this._keycloak.init({
            adapter: 'default',
            promiseType: 'native'
        }).then((isAuthed) => {
            console.log('Authentication Status: ', isAuthed);
            this._isAuthenticated.next(isAuthed);

            return isAuthed;
        }, (error) => {
            console.log('Error in init: ', error);
            this._isAuthenticated.next(false);

            return false;
        });
    }

    public getRoles(): string[] {
        return this._keycloak.realmAccess.roles;
    }

    public async loginUser(): Promise<boolean> {
        if (this.platformInfo.isCurrentPlatformApp()) {
            return await this.mobileKeycloakLogin().then((token) => {
                if (token) {
                    this._isAuthenticated.next(true);

                    this.loadUserInfo().then(data => {
                        console.log('User ', data);
                    });
                    return true;
                } else {
                    this._isAuthenticated.next(false);
                    return false;
                }
            });
        } else {
            return this._keycloak.login().then(() => {
                this._isAuthenticated.next(true);
                return true;
            });
        }
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


    private static redirectUri = 'http://hogarama-login.test/';
    private static realmnUrl = `${environment.keycloak.url}realms/${environment.keycloak.realm}`;
    private static loginUrl = `${AuthenticationService.realmnUrl}/protocol/openid-connect/auth`;
    private static registerUrl = `${AuthenticationService.realmnUrl}/protocol/openid-connect/registrations`;

    public mobileKeycloakLogin(): Promise<any> {
        return new Promise((resolve, reject) => {
            const url = this.getLoginUrlWithParameters(AuthenticationService.loginUrl);

            const options: InAppBrowserOptions = {
                location: 'yes'
            };

            const browser = this.inAppBrowser.create(url, '_blank', options);

            const listener = browser.on('loadstart').subscribe((event: any) => {
                const callback = encodeURI(event.url);
                //Check the redirect uri
                if (callback.indexOf(AuthenticationService.redirectUri) > -1) {
                    listener.unsubscribe();
                    browser.close();

                    const params = new URLSearchParams(event.url.slice(event.url.indexOf('?') + 1));
                    const paramsObj = {
                        code: params.get('code'),
                        state: params.get('state')
                    };

                    this.getAccessToken(paramsObj)
                        .then((token) => {
                            this.storage.set('KEYCLOAK_OAUTH_TOKEN', token);
                            resolve(token);
                        });
                }
            });
        });
    }

    getLoginUrlWithParameters(url: string): string {
        const state = uuidv4();
        const nonce = uuidv4();
        const responseMode = 'query';
        const responseType = 'code';
        const scope = 'openid';
        return url +
            '?client_id=' + encodeURIComponent(environment.keycloak.clientId) +
            '&state=' + encodeURIComponent(state) +
            '&redirect_uri=' + encodeURIComponent(AuthenticationService.redirectUri) +
            '&response_mode=' + encodeURIComponent(responseMode) +
            '&response_type=' + encodeURIComponent(responseType) +
            '&scope=' + encodeURIComponent(scope) +
            '&nonce=' + encodeURIComponent(nonce);
    }


    getAccessToken(authorizationResponse: { code: string, state: string }): Promise<any> {
        const URI = AuthenticationService.realmnUrl + '/protocol/openid-connect/token';
        const body = new HttpParams()
            .set('grant_type', 'authorization_code')
            .set('code', authorizationResponse.code)
            .set('client_id', encodeURIComponent(environment.keycloak.clientId))
            .set('redirect_uri', AuthenticationService.redirectUri);

        const headers = new HttpHeaders()
            .set('Content-Type', 'application/x-www-form-urlencoded');

        const clientSecret = environment.keycloak.credentials.secret;
        if (environment.keycloak.clientId && clientSecret) {
            headers.set('Authorization', 'Basic ' + btoa(environment.keycloak.clientId + ':' + clientSecret));
        }

        return this.http.post(URI, body, {headers: headers}).toPromise()
            .then((token: any) => {
                token.iat = (new Date().getTime() / 1000) - 10;
                console.log('Got token: ', token);
                return token;
            }).catch(error => console.log('Error in token', error));
    }

    async loadUserInfo(): Promise<any> {
        const token = await this.storage.get('KEYCLOAK_OAUTH_TOKEN');

        const url = AuthenticationService.realmnUrl + '/protocol/openid-connect/userinfo';
        const headers = new HttpHeaders()
            .set('Authorization', 'Bearer ' + token.access_token)
            .set('Content-Type', 'application/json');

        return this.http.get(url, {headers: headers}).toPromise();
    }
}