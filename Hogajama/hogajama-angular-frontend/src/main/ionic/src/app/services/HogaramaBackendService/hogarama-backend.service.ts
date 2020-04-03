import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
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

    public actors = new HogaramaBackendPath<Actor>(this.http, 'rest/unitmanagement/actor');
    public sensors = new HogaramaBackendPath<Sensor>(this.http, 'rest/unitmanagement/sensor');
    public units = new HogaramaBackendPath<Unit>(this.http, 'rest/unitmanagement/unit');
    public users = new HogaramaBackendPath<UserData>(this.http, 'rest/unitmanagement/user', false);

    constructor(private http: HttpClient) {
    }
}
