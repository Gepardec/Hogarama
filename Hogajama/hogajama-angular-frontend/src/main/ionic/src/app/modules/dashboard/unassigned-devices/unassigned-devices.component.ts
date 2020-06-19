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
    sensors: Sensor[] = [];
    actors: Actor[] = [];


    constructor(
        private rs: HogaramaBackendService,
        private authService: AuthenticationService,
        private router: Router) {
    }

    async ngOnInit() {
        try {
            this.units = await this.rs.units.getAllByBearer();
        } catch (error) {
            console.error(error);
        }
        try {
            this.sensors = await this.rs.sensors.getAllByBearer();
        } catch (error) {
            console.error(error);
        }
        try {
            // TODO something else
            // this.username = (await this.rs.users.getByBearer()).name;
        } catch (error) {
            console.error(error);
        }
    }

}
