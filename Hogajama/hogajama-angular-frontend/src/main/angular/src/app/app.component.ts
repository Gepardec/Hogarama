import {Component} from '@angular/core';
import { AuthenticationService } from './services/AuthenticationService/authentication.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'hogajama-angular-frontend';

  constructor(private authService: AuthenticationService) {
    //this.authService.loginUser();
  }
}
