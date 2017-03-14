import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AppComponent }         from './app.component';
import { TeamComponent } from './team.component';
import { ChartsComponent }      from './charts.component';

const routes: Routes = [
  //{ path: '', redirectTo: '/index', pathMatch: 'full' },
  //{ path: 'index',  component: AppComponent },
  { path: '', redirectTo: '/team', pathMatch: 'full' },
  { path: 'team',  component: TeamComponent },
  { path: 'charts', component: ChartsComponent }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
