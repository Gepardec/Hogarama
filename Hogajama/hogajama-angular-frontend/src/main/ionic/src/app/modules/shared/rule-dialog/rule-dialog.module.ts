import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RuleDialogComponent} from './rule-dialog.component';
import {MatButtonModule, MatFormFieldModule, MatInputModule, MatSelectModule} from '@angular/material';
import {FormsModule} from '@angular/forms';
import {IonicModule} from '@ionic/angular';

@NgModule({
  declarations: [RuleDialogComponent],
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
export class RuleDialogModule { }
