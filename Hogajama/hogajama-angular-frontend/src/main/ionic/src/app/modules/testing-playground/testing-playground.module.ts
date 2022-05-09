import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {RouterModule, Routes} from '@angular/router';

import {IonicModule} from '@ionic/angular';

import {TestingPlaygroundPage} from './testing-playground.page';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatDialogModule } from '@angular/material/dialog';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { MatTabsModule } from '@angular/material/tabs';
import { MatSelectModule } from '@angular/material/select';
import {SharedModule} from '../shared/shared.module';
import {SensorDialogModule} from '../shared/sensor-dialog/sensor-dialog.module';
import {SensorDialogComponent} from '../shared/sensor-dialog/sensor-dialog.component';
import {UnitDialogModule} from '../shared/unit-dialog/unit-dialog.module';
import {UnitDialogComponent} from '../shared/unit-dialog/unit-dialog.component';
import {ActorDialogComponent} from '../shared/actor-dialog/actor-dialog.component';
import {ActorDialogModule} from '../shared/actor-dialog/actor-dialog.module';
import {RuleDialogComponent} from '../shared/rule-dialog/rule-dialog.component';
import {RuleDialogModule} from '../shared/rule-dialog/rule-dialog.module';

const routes: Routes = [
    {
        path: '',
        component: TestingPlaygroundPage
    }
];

@NgModule({
    entryComponents: [SensorDialogComponent, UnitDialogComponent, ActorDialogComponent, RuleDialogComponent],
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
        ActorDialogModule,
        RuleDialogModule
    ],
    declarations: [TestingPlaygroundPage]
})
export class TestingPlaygroundPageModule {
}
