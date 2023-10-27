import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import { firstValueFrom } from 'rxjs';

type WrappedDto<X> = { response: X };

export class HogaramaBackendPath<responseType> {
  public static baseUrl = environment.backendUrl;

  constructor(private http: HttpClient, private pathUrl: string, private wrappedByDto: boolean = true) {
  }

  public getByBearer(): Promise<responseType> {
    return this.mapPromiseResult(
      firstValueFrom(this.http.get<WrappedDto<responseType>>(`${HogaramaBackendPath.baseUrl}/${this.pathUrl}`))
    );
  }
  public get(id: string|number): Promise<responseType> {
    return this.mapPromiseResult(
      firstValueFrom(this.http.get<WrappedDto<responseType>>(`${HogaramaBackendPath.baseUrl}/${this.pathUrl}/${id}`))
    );
  }
  public getAllByBearer(): Promise<responseType[]> {
    return this.mapPromiseResult(
      firstValueFrom(this.http.get<WrappedDto<responseType[]>>(`${HogaramaBackendPath.baseUrl}/${this.pathUrl}`))
    );
  }
  public getAllForUser(): Promise<responseType[]> {
    return this.mapPromiseResult(
      firstValueFrom(this.http.get<WrappedDto<responseType[]>>(`${HogaramaBackendPath.baseUrl}/${this.pathUrl}`))
    );
  }
  public put(data: responseType): Promise<responseType> {
    return this.mapPromiseResult(
      firstValueFrom(this.http.put<WrappedDto<responseType>>(`${HogaramaBackendPath.baseUrl}/${this.pathUrl}`, data))
    );
  }
  public chat(data: responseType[]): Promise<responseType> {
    return this.mapPromiseResult(
        firstValueFrom(this.http.post<WrappedDto<responseType>>(`${HogaramaBackendPath.baseUrl}/${this.pathUrl}`, data))
    );
  }
  public patch(id: string|number, data: responseType): Promise<responseType> {
    return this.mapPromiseResult(
      firstValueFrom(this.http.patch<WrappedDto<responseType>>(`${HogaramaBackendPath.baseUrl}/${this.pathUrl}/${id}`, data))
    );
  }
  public delete(id: string|number): Promise<responseType> {
    return this.mapPromiseResult(
      firstValueFrom(this.http.delete<WrappedDto<responseType>>(`${HogaramaBackendPath.baseUrl}/${this.pathUrl}/${id}`))
    );
  }

  private mapPromiseResult<anyType>(promise: Promise<WrappedDto<anyType>>): Promise<anyType> {
    // @ts-ignore, weil type nicht aus this.wrappedByDto hergeleitet werden kann
    return promise.then((resp) => {
      if (this.wrappedByDto) {
        return resp.response;
      } else {
        return resp;
      }
    });
  }
}
