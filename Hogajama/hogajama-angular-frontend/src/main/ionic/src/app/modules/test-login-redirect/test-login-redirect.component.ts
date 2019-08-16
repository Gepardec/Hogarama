import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/services/AuthenticationService/authentication.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-test-login-redirect',
  templateUrl: './test-login-redirect.component.html',
  styleUrls: ['./test-login-redirect.component.scss']
})
export class TestLoginRedirectComponent implements OnInit {
  isAuthenticated: boolean;

  constructor(public authService: AuthenticationService, private router: Router) {
    authService.isAuthenticated().subscribe(isAuthed => {
      this.isAuthenticated = isAuthed;
    });
    this.isAuthenticated = authService.isCurrentlyAuthenticated();
  }

  ngOnInit() {
    this.authService.isKeycloakAuthenticated().then(
      () => this.router.navigateByUrl('/home'),
      () => alert('Login failure!')
    );
  }

  onLoginClickTest(): void {
    this.authService.loginUser().then(
      () => this.router.navigateByUrl('/home'),
      () => alert('Login failure!')
    );
  }

  onLogoutClickTest(): void {
    this.authService.logoutUser().then(
      () => alert('Logged out!'),
      () => alert('Logout failure!')
    );
  }

}
