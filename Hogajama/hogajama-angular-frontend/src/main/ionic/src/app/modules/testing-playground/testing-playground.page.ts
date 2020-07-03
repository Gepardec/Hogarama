import { Component, OnInit } from '@angular/core';
import {AuthenticationService} from "../../services/AuthenticationService/authentication.service";
import {ToastController} from "@ionic/angular";
import {MatDialog, MatTableDataSource} from "@angular/material";
import {Sensor} from "../../shared/models/Sensor";
import {HogaramaBackendService} from "../../services/HogaramaBackendService/hogarama-backend.service";
import {SensorDialogComponent} from "../shared/sensor-dialog/sensor-dialog.component";
import {Unit} from "../../shared/models/Unit";
import {Actor} from "../../shared/models/Actor";
import {UnitDialogComponent} from "../shared/unit-dialog/unit-dialog.component";
import {ActorDialogComponent} from "../shared/actor-dialog/actor-dialog.component";

@Component({
  selector: 'app-testing-playground',
  templateUrl: './testing-playground.page.html',
  styleUrls: ['./testing-playground.page.scss'],
})
export class TestingPlaygroundPage implements OnInit {
    sensorsDisplayedColumns: string[] = ['id', 'name', 'deviceId', 'unitId', 'sensorTypeId', 'actions'];
    sensorsDataSource: MatTableDataSource<Sensor>= new MatTableDataSource<Sensor>();

    actorsDisplayedColumns: string[] = ['id', 'name', 'deviceId', 'unitId', 'queueName', 'actions'];
    actorsDataSource: MatTableDataSource<Actor>= new MatTableDataSource<Actor>();

    unitsDisplayedColumns: string[] = ['id', 'name', 'description', 'isDefault', 'ownerId', 'actions'];
    unitsDataSource: MatTableDataSource<Unit>= new MatTableDataSource<Unit>();
    constructor(
      public authService: AuthenticationService,
      public toastController: ToastController,
      private backend: HogaramaBackendService,
      public dialog: MatDialog
    ) { }

    async ngOnInit() {
        await this.reloadData();
    }

    private async reloadData() {
        await this.reloadSensors();
        await this.reloadActors();
        await this.reloadUnits();
    }

    private async reloadUnits() {
        try {
            this.unitsDataSource.data = await this.backend.units.getAllForOwner();
        } catch (e) {
        }
    }

    private async reloadActors() {
        try {
            this.actorsDataSource.data = await this.backend.actors.getAllForOwner();
        } catch (e) {
        }
    }

    private async reloadSensors() {
        try {
            this.sensorsDataSource.data = await this.backend.sensors.getAllForOwner();
        } catch (e) {
        }
    }

    onLoginClickTest(): void {
        this.authService.loginUser().then(
            () => this.presentToast('Logged In!'),
            (reason) => this.presentToast('Log in failure: ' + reason)
        );
    }

    onLogoutClickTest(): void {
        this.authService.logoutUser().then(
            () => this.presentToast('Logged out!'),
            () => this.presentToast('Logout failure!')
        );
    }

    async presentToast(text: string) {
        const toast = await this.toastController.create({
            message: text,
            duration: 2000
        });
        toast.present();
    }

    copyBearerToClipboard() {
        document.execCommand('copy');
        this.presentToast('Copied to clipboard')
    }

    async deleteSensor(id: number) {
        try {
            let result = await this.backend.sensors.delete(id);
            console.log(result);
            this.presentToast('Sensor deleted');
            this.reloadSensors();
        } catch (e) {
            console.log(e);
            this.presentToast('Sensor delete failed');
        }
    }

    async deleteUnit(id: number) {
        try {
            let result = await this.backend.sensors.delete(id);
            console.log(result);
            this.presentToast('Unit deleted');
            this.reloadUnits();
        } catch (e) {
            console.log(e);
            this.presentToast('Unit delete failed');
        }
    }

    async deleteActor(id: number) {
        try {
            let result = await this.backend.actors.delete(id);
            console.log(result);
            this.presentToast('Actor deleted');
            this.reloadActors();
        } catch (e) {
            console.log(e);
            this.presentToast('Actor delete failed');
        }
    }

    addNewSensor() {
        let dialogRef = this.dialog.open(SensorDialogComponent, {
            height: '500px',
            width: '400px',
        });
        dialogRef.afterClosed().subscribe(result => {
            console.log(result);
            this.presentToast('Sensor added');
            this.reloadSensors();
        })
    }

    addNewUnit() {
        let dialogRef = this.dialog.open(UnitDialogComponent, {
            height: '500px',
            width: '400px',
        });
        dialogRef.afterClosed().subscribe(result => {
            console.log(result);
            this.presentToast('Unit added');
            this.reloadUnits();
        });
    }

    addNewActor() {
        let dialogRef = this.dialog.open(ActorDialogComponent, {
            height: '500px',
            width: '400px',
        });
        dialogRef.afterClosed().subscribe(result => {
            console.log(result);
            this.presentToast('Actor added');
            this.reloadActors();
        });
    }

    editSensor(sensor: Sensor) {
        let dialogRef = this.dialog.open(SensorDialogComponent, {
            height: '500px',
            width: '400px',
            data: sensor
        });
        dialogRef.afterClosed().subscribe(result => {
            console.log(result);
            this.presentToast('Sensor edited');
            this.reloadSensors();
        })
    }

    editUnit(unit: Unit) {
        let dialogRef = this.dialog.open(UnitDialogComponent, {
            height: '500px',
            width: '400px',
            data: unit
        });
        dialogRef.afterClosed().subscribe(result => {
            console.log(result);
            this.presentToast('Unit edited');
            this.reloadUnits();
        })
    }

    editActor(actor: Actor) {
        let dialogRef = this.dialog.open(ActorDialogComponent, {
            height: '500px',
            width: '400px',
            data: actor
        });
        dialogRef.afterClosed().subscribe(result => {
            console.log(result);
            this.presentToast('Unit edited');
            this.reloadActors();
        })
    }
}
