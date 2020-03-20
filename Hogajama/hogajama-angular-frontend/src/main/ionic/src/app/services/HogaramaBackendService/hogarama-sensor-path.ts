import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {UserData} from 'src/app/shared/models/UserData';
import {Sensor} from "../../shared/models/Sensor";
import {environment} from "../../../environments/environment";

export class HogaramaBackendPath<responseType> {
  public static baseUrl = environment.backendUrl;

  constructor(private http: HttpClient, private pathUrl: string) {
  }

  public get(id: string): Promise<{response: responseType}> {
    return this.http.get<{ response: responseType }>(`${HogaramaBackendPath.baseUrl}/${this.pathUrl}/${id}`).toPromise();
  }
  public getAll(): Promise<{response: responseType[]}> {
    return this.http.get<{ response: responseType[] }>(`${HogaramaBackendPath.baseUrl}/${this.pathUrl}/all`).toPromise();
  }
  public post(data: responseType): Promise<{response: responseType}> {
    return this.http.post<{ response: responseType }>(`${HogaramaBackendPath.baseUrl}/${this.pathUrl}`, data).toPromise();
  }
  public put(id: string, data: responseType): Promise<{response: responseType}> {
    return this.http.put<{ response: responseType }>(`${HogaramaBackendPath.baseUrl}/${this.pathUrl}/${id}`, data).toPromise();
  }
  public delete(id: string): Promise<{response: responseType}> {
    return this.http.delete<{ response: responseType }>(`${HogaramaBackendPath.baseUrl}/${this.pathUrl}/${id}`).toPromise();
  }

  public getAllSensors(): Observable<{ response: Sensor[] }> {
    return this.http.get<{ response: Sensor[] }>(`${HogaramaBackendPath.baseUrl}/rest/v2/sensor/all`);
  }

  public getAllSensorsForOwner(): Observable<{ response: Sensor[] }> {
    return this.http.get<{ response: Sensor[] }>(`${HogaramaBackendPath.baseUrl}/rest/v2/sensor`);
  }
}
