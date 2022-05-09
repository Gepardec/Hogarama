import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {UnitDialogComponent} from "./unit-dialog.component";
import {MatButtonModule, MatFormFieldModule, MatInputModule, MatSelectModule} from "@angular/material";
import {FormsModule} from "@angular/forms";
import {IonicModule} from "@ionic/angular";

@NgModule({
  declarations: [UnitDialogComponent],
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    FormsModule,
    IonicModule
  ]
})
export class UnitDialogModule { }
