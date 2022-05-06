import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Routes, RouterModule } from '@angular/router';

import { IonicModule } from '@ionic/angular';

import { ConfigDeviceSetupCompletePage } from './config-device-setup-complete.page';
import { MatButtonModule, MatIconModule } from '@angular/material';
import { BackButtonModule } from 'src/app/directives/back-button/back-button.module';

const routes: Routes = [
  {
    path: '',
    component: ConfigDeviceSetupCompletePage
  }
];

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    RouterModule.forChild(routes),
    BackButtonModule,
    MatButtonModule,
    MatIconModule
  ],
  declarations: [ConfigDeviceSetupCompletePage]
})
export class ConfigDeviceSetupCompletePageModule {}
