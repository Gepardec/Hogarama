import {HttpClient} from '@angular/common/http';
import {environment} from "../../../environments/environment";

export class HogaramaBackendPath<responseType> {
  public static baseUrl = environment.backendUrl;

  constructor(private http: HttpClient, private pathUrl: string) {
  }

  public getByBearer(): Promise<responseType> {
    return this.mapPromiseResult(
      this.http.get<{ response: responseType }>(`${HogaramaBackendPath.baseUrl}/${this.pathUrl}`).toPromise()
    );
  }
  public get(id: string|number): Promise<responseType> {
    return this.mapPromiseResult(
      this.http.get<{ response: responseType }>(`${HogaramaBackendPath.baseUrl}/${this.pathUrl}/${id}`).toPromise()
    );
  }
  public getAllByBearer(): Promise<responseType[]> {
    return this.mapPromiseResult(
      this.http.get<{ response: responseType[] }>(`${HogaramaBackendPath.baseUrl}/${this.pathUrl}`).toPromise()
    );
  }
  public getAll(): Promise<responseType[]> {
    return this.mapPromiseResult(
      this.http.get<{ response: responseType[] }>(`${HogaramaBackendPath.baseUrl}/${this.pathUrl}/all`).toPromise()
    );
  }
  public put(data: responseType): Promise<responseType> {
    return this.mapPromiseResult(
      this.http.put<{ response: responseType }>(`${HogaramaBackendPath.baseUrl}/${this.pathUrl}`, data).toPromise()
    );
  }
  public patch(id: string|number, data: responseType): Promise<responseType> {
    return this.mapPromiseResult(
      this.http.patch<{ response: responseType }>(`${HogaramaBackendPath.baseUrl}/${this.pathUrl}/${id}`, data).toPromise()
    );
  }
  public delete(id: string|number): Promise<responseType> {
    return this.mapPromiseResult(
      this.http.delete<{ response: responseType }>(`${HogaramaBackendPath.baseUrl}/${this.pathUrl}/${id}`).toPromise()
    );
  }
  
  private mapPromiseResult<anyType>(promise: Promise<{response: anyType}>): Promise<anyType> {
    return promise.then(({response}) => {
      return response;
    });
  }

}
