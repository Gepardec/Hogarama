import { Injectable, isDevMode } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class HogaramaBackendService {
  public baseUrl: string = 'https://hogajama-57-hogarama.cloud.itandtel.at/hogajama-rs';

  constructor(private http: HttpClient) {
    /*if (isDevMode()) {
      this.baseUrl = 'http://localhost:8080/hogajama-rs';
    }*/
  }

  public getAllSensors(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/rest/sensor`);
  }
}
