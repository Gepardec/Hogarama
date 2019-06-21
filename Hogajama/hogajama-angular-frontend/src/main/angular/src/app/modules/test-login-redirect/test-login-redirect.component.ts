import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/services/AuthenticationService/authentication.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-test-login-redirect',
  templateUrl: './test-login-redirect.component.html',
  styleUrls: ['./test-login-redirect.component.scss']
})
export class TestLoginRedirectComponent implements OnInit {

  constructor(private authService: AuthenticationService, private router: Router) { }

  ngOnInit() {
    this.authService.isKeycloakAuthenticated().then(
      () => this.router.navigateByUrl('/testPrototype'),
      () => alert('Login failure!')
    );
  }

  onLoginClickTest(): void {
    this.authService.loginUser().then(
      () => this.router.navigateByUrl('/testPrototype'),
      () => alert('Login failure!')
    );
  }

}
