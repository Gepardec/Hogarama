import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
// tslint:disable-next-line: variable-name
  private _isAuthenticated: Subject<boolean> = new Subject();
  private _isCurrentlyAuthenticated: boolean;

  constructor() {
    this.isAuthenticated().subscribe((status: boolean) => {
      this._isCurrentlyAuthenticated = status;
    });
  }

  public loginUser(): Promise<boolean> {
    return new Promise<boolean>((resolve, reject) => {
      let loginStatus = true;
      this._isAuthenticated.next(loginStatus);
      resolve(loginStatus);
    });
  }

  public logoutUser(): Promise<boolean> {
    return new Promise<boolean>((resolve, reject) => {
      let loginStatus = false;
      this._isAuthenticated.next(loginStatus);
      resolve(loginStatus);
    });
  }

  public isAuthenticated(): Observable<boolean> {
    return this._isAuthenticated.asObservable();
  }

  public isCurrentlyAuthenticated(): boolean {
    return this._isCurrentlyAuthenticated;
  }
}
