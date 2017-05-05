import { Injectable }    from '@angular/core';
import { Headers, Http } from '@angular/http';

import 'rxjs/add/operator/toPromise';

import { Habarama } from './habarama';

@Injectable()
export class HabaramaService {

  private headers = new Headers({'Content-Type': 'application/json'});
  private heroesUrl = '/hogajama-rs/rest';  // URL to web api

  constructor(private http: Http) { }

  getHabaramas(): Promise<Habarama[]> {
    return this.http.get(this.heroesUrl + '/sensor/mongodb/')
               .toPromise()
               .then(response => response.json().data as Habarama[])
               .catch(this.handleError);
  }

  getHabarama(id: number): Promise<Habarama> {
    const url = `${this.heroesUrl}/sensor/mongodb/${id}`;
    return this.http.get(url)
      .toPromise()
      .then(response => response.json().data as Habarama)
      .catch(this.handleError);
  }

  delete(id: number): Promise<void> {
/*    const url = `${this.heroesUrl}/${id}`;
    return this.http.delete(url, {headers: this.headers})
      .toPromise()
      .then(() => null)
      .catch(this.handleError);*/
      return null;
  }
  create(name: string): Promise<Habarama> {
/*    return this.http
      .post(this.heroesUrl, JSON.stringify({name: name}), {headers: this.headers})
      .toPromise()
      .then(res => res.json().data)
      .catch(this.handleError);*/
      return null;
  }
  update(habarama: Habarama): Promise<Habarama> {
/*    const url = `${this.heroesUrl}/${hero.id}`;
    return this.http
      .put(url, JSON.stringify(hero), {headers: this.headers})
      .toPromise()
      .then(() => hero)
      .catch(this.handleError);*/
      return null;
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }
}
