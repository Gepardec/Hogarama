import {Component, OnInit} from '@angular/core';
import {HogaramaBackendService} from 'src/app/services/HogaramaBackendService/hogarama-backend.service';
import {AuthenticationService} from '../../services/AuthenticationService/authentication.service';
import {Router} from '@angular/router';
import {Sensor} from '../../shared/models/Sensor';
import {Actor} from '../../shared/models/Actor';
import { Unit } from 'src/app/shared/models/Unit';
import {getCompleteUnits, UnitWithSensorsAndActors} from '../../shared/models/UnitWithSensorsAndActors';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  units: Unit[] = [];
  sensors: Sensor[] = [];
  actors: Actor[] = [];

  defaultUnit: UnitWithSensorsAndActors;
  complUnits: UnitWithSensorsAndActors[] = [];
  username = '';

  constructor(
    private rs: HogaramaBackendService,
    private authService: AuthenticationService,
    private router: Router) {
  }

  async ngOnInit() {
    const unitsPromise = this.rs.units.getAllByBearer()
        .then(units => {this.units = units; })
        .catch(e => console.error("Failed to laod units! ", e));
    const sensorsPromise = this.rs.sensors.getAllByBearer()
        .then(sensors => {this.sensors = sensors; })
        .catch(e => console.error("Failed to laod sensors! ", e));
    const actorsPromise = this.rs.actors.getAllByBearer()
        .then(actors => {this.actors = actors; })
        .catch(e => console.error("Failed to laod actors! ", e));

    this.rs.users.getByBearer()
        .then(user => {this.username = user.name; })
        .catch(e => console.error("Failed to laod users! ", e));

    await Promise.all([unitsPromise, sensorsPromise, actorsPromise]);

    [this.defaultUnit, ...this.complUnits] = getCompleteUnits(this.units, this.sensors, this.actors);
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
