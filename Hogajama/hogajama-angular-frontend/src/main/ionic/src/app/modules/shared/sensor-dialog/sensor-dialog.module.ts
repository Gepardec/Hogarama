import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {SensorDialogComponent} from "./sensor-dialog.component";
import {MatButtonModule, MatFormFieldModule, MatInputModule} from "@angular/material";
import {FormsModule} from "@angular/forms";
import {IonicModule} from "@ionic/angular";

@NgModule({
  declarations: [SensorDialogComponent],
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    FormsModule,
    IonicModule
  ]
})
export class SensorDialogModule { }
