import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Routes, RouterModule } from '@angular/router';

import { IonicModule } from '@ionic/angular';

import { ConfigConnectToDeviceInfoPage } from './config-connect-to-device-info.page';
import { MatButtonModule } from '@angular/material';
import { BackButtonModule } from 'src/app/directives/back-button/back-button.module';

const routes: Routes = [
  {
    path: '',
    component: ConfigConnectToDeviceInfoPage
  }
];

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    RouterModule.forChild(routes),
    BackButtonModule,
    MatButtonModule
  ],
  declarations: [ConfigConnectToDeviceInfoPage]
})
export class ConfigConnectToDeviceInfoPageModule {}
