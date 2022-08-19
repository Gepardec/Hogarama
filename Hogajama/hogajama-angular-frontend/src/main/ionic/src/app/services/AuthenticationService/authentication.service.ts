import {Injectable} from '@angular/core';
import {ToastController} from '@ionic/angular';
import Keycloak, {KeycloakError} from 'keycloak-js';
import {BehaviorSubject, Observable} from 'rxjs';
import {environment} from '../../../environments/environment';
import {PlatformInfoService} from '../PlatformInfoService/platform-info.service';
import {KeycloakModel} from '../../shared/models/KeycloakModel';

@Injectable({
    providedIn: 'root'
})
export class AuthenticationService {

    private static LOCALSTORAGE_KC_TOKEN: string = 'kc_token'
    private static LOCALSTORAGE_KC_REFRESH_TOKEN: string = 'kc_refreshToken'
    private _isAuthenticated: BehaviorSubject<boolean> = new BehaviorSubject(false);
    private _keycloak: Keycloak = null;

    constructor(private platformInfo: PlatformInfoService,
                private toastController: ToastController) {}

    public async init(keycloakSettings: KeycloakModel) {
        if(environment.dummySecurity === true){
            await this.presentNotification('Attention! Dummy security active. Do not use it in production.')
            this._isAuthenticated.next(true);
            return;
        }
        const kcUrl = keycloakSettings.authServerUrl;
        if (!kcUrl) {
            await this.presentError('Failed to initialize Keycloak! ', 'URL is not defined')
            return;
        }
        const kcRealm = keycloakSettings.realm;
        if (!kcRealm) {
            await this.presentError('Failed to initialize Keycloak! ', 'Realm is not defined')
            return;
        }
        const kcClientIdFrontend = keycloakSettings.clientIdFrontend;
        if (!kcClientIdFrontend) {
            await this.presentError('Failed to initialize Keycloak! ', 'Client ID is not defined')
            return;
        }

        this._keycloak = new Keycloak({
            url: kcUrl,
            realm: kcRealm,
            clientId: kcClientIdFrontend
        })
        this._keycloak.onAuthSuccess = () => {
            this.saveKeycloakTokens()
            this._isAuthenticated.next(true);
            this.updateToken();
        }
        this._keycloak.onAuthError = (error: KeycloakError) => {
            console.log("Error occured during authentication! ", error)
            this.presentError("Error occured during authentication! ", error)
        }
        this._keycloak.onAuthLogout = () => {
            this.removeKeycloakTokens();
            this._isAuthenticated.next(false);
        }
        this._keycloak.onTokenExpired = () => {
            this.updateToken();
        }
        this._keycloak.onAuthRefreshSuccess = () => {
            this.saveKeycloakTokens()
        }
        this._keycloak.onAuthRefreshError = () => {
            this._keycloak.clearToken()
            this._isAuthenticated.next(false);
        }
        await this._keycloak.init({
            adapter: this.platformInfo.isCurrentPlatformApp() ? 'cordova' : 'default',
            onLoad: 'check-sso',
            pkceMethod: 'S256',
            token: this.getKcTokenInLocalStorage(),
            refreshToken: this.getKcRefreshTokenInLocalStorage(),
            messageReceiveTimeout: 3000
        }).catch((error) => {
            console.error('Failed to initialize Keycloak! ', error)
            this.presentError('Failed to initialize Keycloak! ', error)
        })
    }

    public getToken(): string {
        return this._keycloak?.token;
    }

    private updateToken() {
        this._keycloak.updateToken(environment.keycloakTokenMinValidity);
    }

    public getRefreshToken(): string {
        return this._keycloak?.refreshToken;
    }

    public getRoles(): string[] {
        return this._keycloak?.realmAccess.roles;
    }

    public loginUser() {
        this._keycloak?.login();
    }

    public logoutUser() {
        this._keycloak?.logout();
    }

    public isAuthenticated(): Observable<boolean> {
        return this._isAuthenticated.asObservable();
    }

    public isCurrentlyAuthenticated(): boolean {
        return this._isAuthenticated.getValue();
    }

    public getKcTokenInLocalStorage(): string {
        const token = localStorage.getItem(AuthenticationService.LOCALSTORAGE_KC_TOKEN);
        return token ? token : '';
    }

    public getKcRefreshTokenInLocalStorage(): string {
        const refreshToken = localStorage.getItem(AuthenticationService.LOCALSTORAGE_KC_REFRESH_TOKEN);
        return refreshToken ? refreshToken : '';
    }

    private saveKeycloakTokens(): void {
        localStorage.setItem(AuthenticationService.LOCALSTORAGE_KC_TOKEN, this.getToken());
        localStorage.setItem(AuthenticationService.LOCALSTORAGE_KC_REFRESH_TOKEN, this.getRefreshToken());
    }

    private removeKeycloakTokens(): void {
        localStorage.removeItem(AuthenticationService.LOCALSTORAGE_KC_TOKEN)
        localStorage.removeItem(AuthenticationService.LOCALSTORAGE_KC_REFRESH_TOKEN)
    }

    async presentError(text: string, error) {
        let errorMessage = ''
        if (error && typeof error == 'string') {
            errorMessage = error
        } else if (error && error.hasOwnProperty('error') && typeof error.error == 'string') {
            errorMessage = error.error
        }

        const toast = await this.toastController.create({
            message: text + errorMessage,
            duration: 4000,
            color: 'danger'
        });
        await toast.present();
    }

    async presentNotification(text: string) {

        const toast = await this.toastController.create({
            message: text,
            duration: 5000,
        });
        await toast.present();
    }
}
