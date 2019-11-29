import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserData } from 'src/app/shared/models/UserData';
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class HogaramaBackendService {
  public baseUrl = environment.backendUrl;

  constructor(private http: HttpClient) {
  }

  public getAllSensors(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/rest/sensor`);
  }

  public getUser(): Observable<UserData> {
    return this.http.get<UserData>(`${this.baseUrl}/rest/user`);
  }
}
