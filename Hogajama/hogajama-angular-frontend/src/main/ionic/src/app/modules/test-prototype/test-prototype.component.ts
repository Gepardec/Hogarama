import {Component, OnInit} from '@angular/core';
import {HogaramaBackendService} from 'src/app/services/HogaramaBackendService/hogarama-backend.service';
import {AuthenticationService} from "../../services/AuthenticationService/authentication.service";
import {Router} from "@angular/router";
import {Sensor} from "../../shared/models/Sensor";

@Component({
  selector: 'app-test-prototype',
  templateUrl: './test-prototype.component.html',
  styleUrls: ['./test-prototype.component.scss']
})
export class TestPrototypeComponent implements OnInit {
  allSensorArr: Sensor[];
  ownerSensorArr: Sensor[];
  error: string;

  constructor(private rs: HogaramaBackendService, private authService: AuthenticationService, private router: Router) {
  }

  async ngOnInit() {
    try {
      this.allSensorArr = await this.rs.sensors.getAll();
    } catch(error) {
      console.error(error);
      this.error = error.message;
    }

    try {
      this.ownerSensorArr = await this.rs.sensors.getAllByBearer();
    } catch(error) {
      console.error(error);
      this.error = error.message;
    }
  }

  logout() {
    this.authService.logoutUser().then(
      () => this.router.navigateByUrl('/testLoginRedirect'),
      () => alert('Logout failure!')
    )
  }
}
