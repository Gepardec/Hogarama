import {Component} from '@angular/core';
import {AuthenticationService} from 'src/app/services/AuthenticationService/authentication.service';
import {DummyUser} from 'src/app/services/DummyUser/dummyuser.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  isAuthenticated: boolean;

  constructor(public authService: AuthenticationService, private router: Router, public dummyUser: DummyUser) {
    authService.isAuthenticated().subscribe(isAuthed => {
      this.isAuthenticated = isAuthed;
    });
    this.isAuthenticated = authService.isCurrentlyAuthenticated();
  }

  loginClick(): void {
    this.authService.loginUser()
  }

  logoutClick(): void {
    this.authService.logoutUser()
  }

}
