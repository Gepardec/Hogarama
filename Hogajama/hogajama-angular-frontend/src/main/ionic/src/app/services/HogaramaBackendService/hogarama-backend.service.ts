import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserData } from 'src/app/shared/models/UserData';

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

  public getAllSensors(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/rest/sensor`);
  }

  public getUser(): Observable<UserData> {
    return this.http.get<UserData>(`${this.baseUrl}/rest/user`);
  }
}
