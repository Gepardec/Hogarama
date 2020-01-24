import { Component, OnInit } from '@angular/core';
import {AuthenticationService} from "../../services/AuthenticationService/authentication.service";
import {ToastController} from "@ionic/angular";
import {MatDialog, MatTableDataSource} from "@angular/material";
import {Sensor} from "../../shared/models/Sensor";
import {HogaramaBackendService} from "../../services/HogaramaBackendService/hogarama-backend.service";
import {SensorDialogComponent} from "../shared/sensor-dialog/sensor-dialog.component";
import {Unit} from "../../shared/models/Unit";
import {Actor} from "../../shared/models/Actor";

@Component({
  selector: 'app-testing-playground',
  templateUrl: './testing-playground.page.html',
  styleUrls: ['./testing-playground.page.scss'],
})
export class TestingPlaygroundPage implements OnInit {
    sensorsDisplayedColumns: string[] = ['id', 'name', 'deviceId', 'unitId', 'sensorTypeId', 'actions'];
    sensorsDataSource: MatTableDataSource<Sensor>= new MatTableDataSource<Sensor>();

    actorsDisplayedColumns: string[] = ['id', 'name', 'deviceId', 'unitId', 'actions'];
    actorsDataSource: MatTableDataSource<Actor>= new MatTableDataSource<Actor>();

    unitsDisplayedColumns: string[] = ['id', 'name', 'description', 'isDefault', 'ownerId', 'actions'];
    unitsDataSource: MatTableDataSource<Unit>= new MatTableDataSource<Unit>();
    constructor(
      private authService: AuthenticationService,
      public toastController: ToastController,
      private backend: HogaramaBackendService,
      public dialog: MatDialog
    ) { }

    async ngOnInit() {
        await this.reloadSensors();
    }

    private async reloadSensors() {
        try {
            this.sensorsDataSource.data = await this.backend.sensors.getAll();
        } catch (e) {
        }
        try {
            this.actorsDataSource.data = await this.backend.actors.getAll();
        } catch (e) {
        }
        try {
            this.unitsDataSource.data = await this.backend.units.getAll();
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

    async deleteSensor(id: number) {
        try {
            let result = await this.backend.sensors.delete(id);
            console.log(result);
            this.presentToast('Sensor deleted');
        } catch (e) {
            console.log(e);
            this.presentToast('Sensor delete failed');
        }
    }

    addNewUnit() {

    }

    editUnit(element: any) {

    }

    deleteUnit(id: any) {

    }

    deleteActor(id: any) {

    }

    editActor(element: any) {

    }

    addNewActor() {

    }
}
