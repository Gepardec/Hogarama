import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {UserData} from 'src/app/shared/models/UserData';
import {Sensor} from "../../shared/models/Sensor";

@Injectable({
  providedIn: 'root'
})
export class HogaramaBackendService {
  public baseUrl = '/hogajama-rs';

  constructor(private http: HttpClient) {
    /*if (isDevMode()) {
      this.baseUrl = 'http://localhost:8080/hogajama-rs';
    }*/
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
