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

      // For testing
      this.units = [
        {
          isDefault: true,
          id: 1
        },{
          isDefault: false,
          id: 2,
          name: 'Gummibaum'
        }
      ]
    }
    try {
      this.sensors = await this.rs.sensors.getAllByBearer();
    } catch(error) {
      console.error(error);

      // For testing
      this.sensors = [
        {
          id: 1,
          unitId: 1,
          sensorTypeId: 1,
          name: 'Sensor 1'
        },
        {
          id: 2,
          unitId: 1,
          sensorTypeId: 1,
          name: 'Sensor 2'
        },
        {
          id: 3,
          unitId: 2,
          sensorTypeId: 1,
          name: 'Sensor 3'
        }
      ]
    }

    [this.defaultUnit, ...this.complUnits] = getCompleteUnits(this.units, this.sensors, []);

    try {
      this.username = (await this.rs.users.getByBearer()).name;
    } catch(error) {
      console.error(error);
    }

    // TODO do we want to redirect to the dashboard if we already have some sensors, units and actors?
    // Answer: no we want to stay on the same page and rename it to 'dashboard' ;)
    // this.redirectToDashboardIfNecessary();
  }

  private redirectToDashboardIfNecessary() {
    if (this.units.length != 0 || this.sensors.length != 0) {
      this.router.navigateByUrl("/dashboard");
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
