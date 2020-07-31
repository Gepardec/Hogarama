import {Component, OnInit} from '@angular/core';
import {HogaramaBackendService} from 'src/app/services/HogaramaBackendService/hogarama-backend.service';
import {AuthenticationService} from "../../services/AuthenticationService/authentication.service";
import {Router} from "@angular/router";
import {Sensor} from "../../shared/models/Sensor";
import { Unit } from 'src/app/shared/models/Unit';
import {getCompleteUnits, UnitWithSensorsAndActors} from "../../shared/models/UnitWithSensorsAndActors";

@Component({
  selector: 'app-test-prototype',
  templateUrl: './test-prototype.component.html',
  styleUrls: ['./test-prototype.component.scss']
})
export class TestPrototypeComponent implements OnInit {
  units: Unit[] = [];
  sensors : Sensor[] = [];

  defaultUnit: UnitWithSensorsAndActors;
  complUnits : UnitWithSensorsAndActors[] = [];
  username: string = '';

  constructor(
    private rs: HogaramaBackendService, 
    private authService: AuthenticationService, 
    private router: Router) {
  }

  async ngOnInit() {
    try {
      this.units = await this.rs.units.getAllByBearer();
    } catch(error) {
      console.error(error);
    }
    try {
      this.sensors = await this.rs.sensors.getAllByBearer();
    } catch(error) {
      console.error(error);
    }

    [this.defaultUnit, ...this.complUnits] = getCompleteUnits(this.units, this.sensors, []);

    try {
      this.username = (await this.rs.users.getByBearer()).name;
    } catch(error) {
      console.error(error);
    }
  }

  openSettings() {

  }

  addPlant() {
    this.router.navigateByUrl('add-plant');
  }

  plantsExist() {
    return this.defaultUnit != null;
  }
}
