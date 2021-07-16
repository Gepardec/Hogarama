import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Routes, RouterModule } from '@angular/router';

import { IonicModule } from '@ionic/angular';

import { ConfigStartInfoPage } from './config-start-info.page';
import { MatButtonModule } from '@angular/material';
import { BackButtonModule } from 'src/app/directives/back-button/back-button.module';

const routes: Routes = [
  {
    path: '',
    component: ConfigStartInfoPage
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
  declarations: [ConfigStartInfoPage]
})
export class ConfigStartInfoPageModule {}
