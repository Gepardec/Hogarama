import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {SensorDialogComponent} from './sensor-dialog.component';
import {MatButtonModule} from '@angular/material/button';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {FormsModule} from '@angular/forms';
import {IonicModule} from '@ionic/angular';

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
