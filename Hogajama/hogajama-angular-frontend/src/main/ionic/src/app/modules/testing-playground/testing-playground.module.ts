import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {RouterModule, Routes} from '@angular/router';

import {IonicModule} from '@ionic/angular';

import {TestingPlaygroundPage} from './testing-playground.page';
import {
    MatButtonModule,
    MatDialogModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatPaginatorModule,
    MatSelectModule,
    MatSortModule,
    MatTableModule,
    MatTabsModule
} from "@angular/material";
import {SharedModule} from "../shared/shared.module";
import {SensorDialogModule} from "../shared/sensor-dialog/sensor-dialog.module";
import {SensorDialogComponent} from "../shared/sensor-dialog/sensor-dialog.component";
import {UnitDialogModule} from "../shared/unit-dialog/unit-dialog.module";
import {UnitDialogComponent} from "../shared/unit-dialog/unit-dialog.component";
import {ActorDialogComponent} from "../shared/actor-dialog/actor-dialog.component";
import {ActorDialogModule} from "../shared/actor-dialog/actor-dialog.module";

const routes: Routes = [
    {
        path: '',
        component: TestingPlaygroundPage
    }
];

@NgModule({
    entryComponents: [SensorDialogComponent, UnitDialogComponent, ActorDialogComponent],
    imports: [
        CommonModule,
        FormsModule,
        IonicModule,
        RouterModule.forChild(routes),
        MatTabsModule,
        SharedModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        MatTableModule,
        MatPaginatorModule,
        MatSortModule,
        MatButtonModule,
        MatDialogModule,
        MatSelectModule,
        SensorDialogModule,
        UnitDialogModule,
        ActorDialogModule
    ],
    declarations: [TestingPlaygroundPage]
})
export class TestingPlaygroundPageModule {
}
