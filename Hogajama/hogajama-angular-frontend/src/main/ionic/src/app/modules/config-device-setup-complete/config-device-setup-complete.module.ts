import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Routes, RouterModule } from '@angular/router';

import { IonicModule } from '@ionic/angular';

import { ConfigDeviceSetupCompletePage } from './config-device-setup-complete.page';
import { MatButtonModule, MatIconModule } from '@angular/material';

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
    MatButtonModule,
    MatIconModule
  ],
  declarations: [ConfigDeviceSetupCompletePage]
})
export class ConfigDeviceSetupCompletePageModule {}
