import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from 'src/app/services/AuthenticationService/authentication.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  isAuthenticated: boolean;

  constructor(public authService: AuthenticationService, private router: Router) {
    authService.isAuthenticated().subscribe(isAuthed => {
      this.isAuthenticated = isAuthed;
    });
    this.isAuthenticated = authService.isCurrentlyAuthenticated();
  }

  ngOnInit() {
  }

  onLoginClickTest(): void {
    this.authService.loginUser().then(
      () => this.router.navigateByUrl('/home'),
      (reason) => console.log(reason)
    );
  }

  onLogoutClickTest(): void {
    this.authService.logoutUser().then(
      () => alert('Logged out!'),
      () => alert('Logout failure!')
    );
  }

}
