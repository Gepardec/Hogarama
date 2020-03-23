import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Routes, RouterModule } from '@angular/router';

import { IonicModule } from '@ionic/angular';

import { ConfigConnectDeviceToWifiPage } from './config-connect-device-to-wifi.page';
import { MatButtonModule, MatFormFieldModule, MatInputModule, MatIconModule, MatSelectModule } from '@angular/material';

const routes: Routes = [
  {
    path: '',
    component: ConfigConnectDeviceToWifiPage
  }
];

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    RouterModule.forChild(routes),
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatSelectModule
  ],
  declarations: [ConfigConnectDeviceToWifiPage]
})
export class ConfigConnectDeviceToWifiPageModule {}
