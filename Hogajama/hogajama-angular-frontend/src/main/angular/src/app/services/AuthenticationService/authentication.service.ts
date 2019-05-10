import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  public isAuthenticated: boolean = true

  constructor() { }
}
