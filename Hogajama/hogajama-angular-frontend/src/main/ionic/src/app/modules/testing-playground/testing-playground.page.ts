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

    unitsDisplayedColumns: string[] = ['id', 'name', 'description', 'isDefault', 'userId', 'actions'];
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
            this.unitsDataSource.data = await this.backend.units.getAllForUser();
        } catch (e) {
        }
    }

    private async reloadActors() {
        try {
            this.actorsDataSource.data = await this.backend.actors.getAllForUser();
        } catch (e) {
        }
    }

    private async reloadSensors() {
        try {
            this.sensorsDataSource.data = await this.backend.sensors.getAllForUser();
        } catch (e) {
        }
    }

    private async reloadRules() {
        try {
            this.rulesDataSource.data = await this.backend.rules.getAllForUser();
        } catch (e) {
        }
    }

    onLoginClickTest(): void {
        this.authService.loginUser()
    }

    onLogoutClickTest(): void {
        this.authService.logoutUser()
    }

    async presentToast(text: string) {
        const toast = await this.toastController.create({
            message: text,
            duration: 2000
        });
        await toast.present();
    }

    async copyBearerToClipboard() {
        document.execCommand('copy');
        await this.presentToast('Copied to clipboard');
    }

    async deleteSensor(id: number) {
        await this.backend.sensors.delete(id).then(() => {
            this.presentToast(`Sensor with id ${id} deleted`);
            this.reloadSensors();
        }).catch(() => { /* errors are handled in MyHttpInterceptor */ });
    }

    async deleteUnit(id: number) {
        await this.backend.units.delete(id).then(() => {
            this.presentToast(`Unit with id ${id} deleted`);
            this.reloadUnits();
        }).catch(() => { /* errors are handled in MyHttpInterceptor */ });
    }

    async deleteActor(id: number) {
        await this.backend.actors.delete(id).then(() => {
            this.presentToast(`Actor with id ${id} deleted`);
            this.reloadActors();
        }).catch(() => { /* errors are handled in MyHttpInterceptor */ });
    }

    async deleteRule(id: number) {
        await this.backend.rules.delete(id).then(() => {
            this.presentToast(`Rule with id ${id} deleted`);
            this.reloadRules();
        }).catch(() => { /* errors are handled in MyHttpInterceptor */ });
    }

    async addNewSensor() {
        let dialogRef = this.dialog.open(SensorDialogComponent, {
            height: '500px',
            width: '400px',
        });
        dialogRef.afterClosed().subscribe((newSensor: Sensor) => {
            if (newSensor !== undefined) {
                this.presentToast('Sensor added');
            }
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

    async addNewActor() {
        let dialogRef = this.dialog.open(ActorDialogComponent, {
            height: '500px',
            width: '400px',
        });
        dialogRef.afterClosed().subscribe((newActor: Actor) => {
            if (newActor !== undefined) {
                this.presentToast('Actor added');
            }
            this.reloadActors();
        });
    }

    async addNewRule() {
        const dialogRef = this.dialog.open(RuleDialogComponent, {
            height: '700px',
            width: '560px',
        });
        dialogRef.afterClosed().subscribe((newRule: Rule) => {
            if (newRule !== undefined) {
                this.presentToast('Rule added');
            }
            this.reloadRules();
        });
    }

    async editSensor(sensor: Sensor) {
        let dialogRef = this.dialog.open(SensorDialogComponent, {
            height: '500px',
            width: '400px',
            data: sensor
        });
        dialogRef.afterClosed().subscribe((editedSensor: Sensor) => {
            if (editedSensor !== undefined) {
                this.presentToast(`Sensor with id ${editedSensor.id} edited`);
            }
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

    async editActor(actor: Actor) {
        let dialogRef = this.dialog.open(ActorDialogComponent, {
            height: '500px',
            width: '400px',
            data: actor
        });
        dialogRef.afterClosed().subscribe((editedActor: Actor) => {
            if (editedActor !== undefined) {
                this.presentToast(`Actor with id ${editedActor.id} edited`);
            }
            this.reloadActors();
        });
    }

    async editRule(rule: Rule) {
        const dialogRef = this.dialog.open(RuleDialogComponent, {
            height: '700px',
            width: '560px',
            data: rule
        });
        dialogRef.afterClosed().subscribe((editedRule: Rule) => {
            if (editedRule !== undefined) {
                this.presentToast(`Rule with id ${editedRule.id} edited`);
            }
            this.reloadRules();
        });
    }
}
