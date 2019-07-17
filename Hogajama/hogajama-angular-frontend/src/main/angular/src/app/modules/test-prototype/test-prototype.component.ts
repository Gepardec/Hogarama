import {Component, OnInit} from '@angular/core';
import {HogaramaBackendService} from 'src/app/services/HogaramaBackendService/hogarama-backend.service';
import {AuthenticationService} from "../../services/AuthenticationService/authentication.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-test-prototype',
  templateUrl: './test-prototype.component.html',
  styleUrls: ['./test-prototype.component.scss']
})
export class TestPrototypeComponent implements OnInit {
  sensorArr: string[];
  error: string;

  constructor(private rs: HogaramaBackendService, private authService: AuthenticationService, private router: Router) {
  }

  ngOnInit() {
    const sub = this.rs.getAllSensors().subscribe((sensorData: string[]) => {
      this.sensorArr = sensorData;
      sub.unsubscribe();
    }, (error) => {
      console.error(error);
      this.error = error.message;
    });
  }

  logout() {
    this.authService.logoutUser().then(
      () => this.router.navigateByUrl('/testLoginRedirect'),
      () => alert('Logout failure!')
    )
  }
}
