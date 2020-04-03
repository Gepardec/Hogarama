import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Routes, RouterModule } from '@angular/router';

import { IonicModule } from '@ionic/angular';

import { ConfigStartInfoPage } from './config-start-info.page';
import { MatButtonModule } from '@angular/material';

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
    MatButtonModule
  ],
  declarations: [ConfigStartInfoPage]
})
export class ConfigStartInfoPageModule {}
