import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {UserData} from 'src/app/shared/models/UserData';
import {Sensor} from "../../shared/models/Sensor";
import {environment} from "../../../environments/environment";
import {HogaramaBackendPath} from "./hogarama-backend-path";
import {Actor} from "../../shared/models/Actor";
import {Unit} from "../../shared/models/Unit";

@Injectable({
    providedIn: 'root'
})
export class HogaramaBackendService {
    public baseUrl = environment.backendUrl;

    public actors = new HogaramaBackendPath<Actor>(this.http, 'rest/v2/actor');
    public sensors = new HogaramaBackendPath<Sensor>(this.http, 'rest/v2/sensor');
    public units = new HogaramaBackendPath<Unit>(this.http, 'rest/v2/unit');
    public users = new HogaramaBackendPath<UserData>(this.http, 'rest/user');

    constructor(private http: HttpClient) {
    }
}
