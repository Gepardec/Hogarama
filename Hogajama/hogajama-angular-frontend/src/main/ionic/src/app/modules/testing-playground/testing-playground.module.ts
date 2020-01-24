import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Routes, RouterModule } from '@angular/router';

import { IonicModule } from '@ionic/angular';

import { TestingPlaygroundPage } from './testing-playground.page';
import {
  MatButtonModule, MatDialogModule,
  MatFormFieldModule,
  MatIconModule,
  MatInputModule,
  MatPaginatorModule, MatSortModule,
  MatTableModule,
  MatTabsModule
} from "@angular/material";
import {SharedModule} from "../shared/shared.module";
import {SensorDialogModule} from "../shared/sensor-dialog/sensor-dialog.module";
import {SensorDialogComponent} from "../shared/sensor-dialog/sensor-dialog.component";

const routes: Routes = [
  {
    path: '',
    component: TestingPlaygroundPage
  }
];

@NgModule({
    entryComponents: [SensorDialogComponent],
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
      SensorDialogModule
  ],
  declarations: [TestingPlaygroundPage]
})
export class TestingPlaygroundPageModule {}
