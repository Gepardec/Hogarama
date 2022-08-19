import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class DummyUser {

    name: string;
    email: string;
    givenName: string;
    familyName: string;

    constructor() {
        this.name = 'Dummy';
        this.email = 'dummy@nowhere';
        this.givenName = 'Dummy';
        this.familyName = 'Franz';
    }

    public isActive(): boolean {
        return environment.dummySecurity === true;
    }
}
