import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Routes, RouterModule } from '@angular/router';

import { IonicModule } from '@ionic/angular';

import { ConfigConnectDeviceToWifiPage } from './config-connect-device-to-wifi.page';
import { MatButtonModule, MatFormFieldModule, MatInputModule, MatIconModule, MatSelectModule } from '@angular/material';
import { BackButtonModule } from 'src/app/directives/back-button/back-button.module';

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
    BackButtonModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatSelectModule
  ],
  declarations: [ConfigConnectDeviceToWifiPage]
})
export class ConfigConnectDeviceToWifiPageModule {}
