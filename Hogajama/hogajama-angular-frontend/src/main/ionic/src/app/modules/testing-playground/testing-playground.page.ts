import { Component, OnInit } from '@angular/core';
import {ToastController} from '@ionic/angular';
import {MatDialog} from '@angular/material/dialog';
import {MatTableDataSource} from '@angular/material/table';
import {AuthenticationService} from '../../services/AuthenticationService/authentication.service';
import {HogaramaBackendService} from '../../services/HogaramaBackendService/hogarama-backend.service';
import {SensorDialogComponent} from '../shared/sensor-dialog/sensor-dialog.component';
import {UnitDialogComponent} from '../shared/unit-dialog/unit-dialog.component';
import {ActorDialogComponent} from '../shared/actor-dialog/actor-dialog.component';
import {RuleDialogComponent} from '../shared/rule-dialog/rule-dialog.component';
import {Sensor} from '../../shared/models/Sensor';
import {Unit} from '../../shared/models/Unit';
import {Actor} from '../../shared/models/Actor';
import {Rule} from '../../shared/models/Rule';

@Component({
  selector: 'app-testing-playground',
  templateUrl: './testing-playground.page.html',
  styleUrls: ['./testing-playground.page.scss'],
})
export class TestingPlaygroundPage implements OnInit {
    sensorsDisplayedColumns: string[] = ['id', 'name', 'deviceId', 'unitId', 'sensorTypeId', 'actions'];
    sensorsDataSource: MatTableDataSource<Sensor> = new MatTableDataSource<Sensor>();

    actorsDisplayedColumns: string[] = ['id', 'name', 'deviceId', 'unitId', 'queueName', 'actions'];
    actorsDataSource: MatTableDataSource<Actor> = new MatTableDataSource<Actor>();

    unitsDisplayedColumns: string[] = ['id', 'name', 'description', 'isDefault', 'ownerId', 'actions'];
    unitsDataSource: MatTableDataSource<Unit> = new MatTableDataSource<Unit>();

    rulesDisplayedColumns: string[] = ['id', 'name', 'description', 'sensorId', 'actorId', 'waterDuration',
        'lowWater', 'unitId', 'actions'];
    rulesDataSource: MatTableDataSource<Rule> = new MatTableDataSource<Rule>();

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
        await this.reloadRules();
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

    private async reloadRules() {
        try {
            this.rulesDataSource.data = await this.backend.rules.getAllForOwner();
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
        this.presentToast('Copied to clipboard');
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
            await this.backend.units.delete(id);
            await this.presentToast(`Unit with id ${id} deleted`);
            await this.reloadUnits();
        } catch (e) {
            console.error('Error occured during deletion of an unit: ', e);
            await this.presentToast('Unit delete failed');
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

    async deleteRule(id: number) {
        try {
            const result = await this.backend.rules.delete(id);
            console.log(result);
            this.presentToast('Rule deleted');
            this.reloadActors();
        } catch (e) {
            console.log(e);
            this.presentToast('Rule delete failed');
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
        });
    }

    async addNewUnit() {
        const dialogRef = this.dialog.open(UnitDialogComponent, {
            height: '500px',
            width: '400px',
        });
        dialogRef.afterClosed().subscribe((newUnit: Unit) => {
            if (newUnit !== undefined) {
                this.presentToast('Unit added');
            }
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

    addNewRule() {
        const dialogRef = this.dialog.open(RuleDialogComponent, {
            height: '700px',
            width: '560px',
        });
        dialogRef.afterClosed().subscribe(result => {
            console.log(result);
            this.presentToast('Rule added');
            this.reloadRules();
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
        });
    }

    async editUnit(unit: Unit) {
        const dialogRef = this.dialog.open(UnitDialogComponent, {
            height: '500px',
            width: '400px',
            data: unit
        });
        dialogRef.afterClosed().subscribe((editedUnit: Unit) => {
            if (editedUnit !== undefined) {
                this.presentToast(`Unit with id ${editedUnit.id} edited`);
            }
            this.reloadUnits();
        });
    }

    editActor(actor: Actor) {
        let dialogRef = this.dialog.open(ActorDialogComponent, {
            height: '500px',
            width: '400px',
            data: actor
        });
        dialogRef.afterClosed().subscribe(result => {
            console.log(result);
            this.presentToast('Actor edited');
            this.reloadActors();
        });
    }

    editRule(rule: Rule) {
        const dialogRef = this.dialog.open(RuleDialogComponent, {
            height: '700px',
            width: '560px',
            data: rule
        });
        dialogRef.afterClosed().subscribe(result => {
            console.log(result);
            this.presentToast('Rule edited');
            this.reloadRules();
        });
    }
}
