import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {UserData} from 'src/app/shared/models/UserData';
import {Sensor} from "../../shared/models/Sensor";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class HogaramaBackendService {
  public baseUrl = environment.backendUrl;

  constructor(private http: HttpClient) {
  }

  public getAllSensors(): Observable<{ response: Sensor[] }> {
    return this.http.get<{ response: Sensor[] }>(`${this.baseUrl}/rest/v2/sensor/all`);
  }

  public getAllSensorsForOwner(): Observable<{ response: Sensor[] }> {
    return this.http.get<{ response: Sensor[] }>(`${this.baseUrl}/rest/v2/sensor`);
  }

  public getUser(): Observable<UserData> {
    return this.http.get<UserData>(`${this.baseUrl}/rest/user`);
  }
}
