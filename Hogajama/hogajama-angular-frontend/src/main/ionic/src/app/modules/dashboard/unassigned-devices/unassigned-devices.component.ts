import {Component, OnInit} from '@angular/core';
import {HogaramaBackendService} from "../../../services/HogaramaBackendService/hogarama-backend.service";
import {AuthenticationService} from "../../../services/AuthenticationService/authentication.service";
import {Router} from "@angular/router";
import {Sensor} from "../../../shared/models/Sensor";
import {Unit} from "../../../shared/models/Unit";
import {Actor} from "../../../shared/models/Actor";


@Component({
    selector: 'app-unassigned-devices',
    templateUrl: './unassigned-devices.component.html',
    styleUrls: ['./unassigned-devices.component.scss'],
})
export class UnassignedDevicesComponent implements OnInit {
    units: Unit[] = [];
    unassignedSensors: Sensor[] = [];
    unassignedActors: Actor[] = [];

    constructor(
        private rs: HogaramaBackendService,
        private authService: AuthenticationService,
        private router: Router) {
    }

    async ngOnInit() {
        try {
            this.units = await this.rs.units.getAllByBearer();
            let defaultUnit = this.units.filter(unit => unit.isDefault)[0];
            let allSensors = await this.rs.sensors.getAllByBearer();
            this.unassignedSensors = allSensors.filter(sensor => sensor.unitId == defaultUnit.id);
            let allActors = await this.rs.actors.getAllByBearer();
            this.unassignedActors = allActors.filter(actor => actor.unitId == defaultUnit.id)
        } catch (error) {
            console.error(error);
        }
    }

}
