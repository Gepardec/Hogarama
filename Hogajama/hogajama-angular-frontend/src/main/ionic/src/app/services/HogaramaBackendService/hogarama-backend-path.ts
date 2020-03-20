import {HttpClient} from '@angular/common/http';
import {environment} from "../../../environments/environment";

type WrappedDto<X> = { response: X };

export class HogaramaBackendPath<responseType> {
  public static baseUrl = environment.backendUrl;

  constructor(private http: HttpClient, private pathUrl: string, private wrappedByDto: boolean = true) {
  }

  public getByBearer(): Promise<responseType> {
    return this.mapPromiseResult(
      this.http.get<WrappedDto<responseType>>(`${HogaramaBackendPath.baseUrl}/${this.pathUrl}`).toPromise()
    );
  }
  public get(id: string|number): Promise<responseType> {
    return this.mapPromiseResult(
      this.http.get<WrappedDto<responseType>>(`${HogaramaBackendPath.baseUrl}/${this.pathUrl}/${id}`).toPromise()
    );
  }
  public getAllByBearer(): Promise<responseType[]> {
    return this.mapPromiseResult(
      this.http.get<WrappedDto<responseType[]>>(`${HogaramaBackendPath.baseUrl}/${this.pathUrl}`).toPromise()
    );
  }
  public getAll(): Promise<responseType[]> {
    return this.mapPromiseResult(
      this.http.get<WrappedDto<responseType[]>>(`${HogaramaBackendPath.baseUrl}/${this.pathUrl}/all`).toPromise()
    );
  }
  public put(data: responseType): Promise<responseType> {
    return this.mapPromiseResult(
      this.http.put<WrappedDto<responseType>>(`${HogaramaBackendPath.baseUrl}/${this.pathUrl}`, data).toPromise()
    );
  }
  public patch(id: string|number, data: responseType): Promise<responseType> {
    return this.mapPromiseResult(
      this.http.patch<WrappedDto<responseType>>(`${HogaramaBackendPath.baseUrl}/${this.pathUrl}/${id}`, data).toPromise()
    );
  }
  public delete(id: string|number): Promise<responseType> {
    return this.mapPromiseResult(
      this.http.delete<WrappedDto<responseType>>(`${HogaramaBackendPath.baseUrl}/${this.pathUrl}/${id}`).toPromise()
    );
  }
  
  private mapPromiseResult<anyType>(promise: Promise<WrappedDto<anyType>>): Promise<anyType> {
    //@ts-ignore, weil type nicht aus this.wrappedByDto hergeleitet werden kann
    return promise.then((resp) => {
      if(this.wrappedByDto) {
        return resp.response;
      } else {
        return resp;
      }
    });
  }
}
