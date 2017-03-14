import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule, Routes } from '@angular/router';
import { environment } from '../environments/environment';
import { APP_BASE_HREF } from '@angular/common';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ChartModule } from 'angular2-highcharts';
import { HighchartsStatic } from 'angular2-highcharts/dist/HighchartsService';
import { Ng2PageScrollModule } from 'ng2-page-scroll';

import { AppComponent } from './app.component';
import { TeamComponent} from './team.component';
import { ChartsComponent } from './charts.component';
import { AppRoutingModule } from './app-routing.module';

import { HabaramaService } from './habarama.service';

// https://github.com/gevgeny/angular2-highcharts/issues/160
export function highchartsFactory() {
  // return [require('highcharts'), require('highcharts/themes/gray')];
  return require('highcharts');
}

@NgModule({
  imports: [
    NgbModule.forRoot(),
/*    ChartModule.forRoot(
      require('highcharts'),
      require('highcharts/themes/gray')
    ),*/
    ChartModule,
    Ng2PageScrollModule.forRoot(),
    BrowserModule,
    FormsModule,
    HttpModule,
    AppRoutingModule
  ],
  declarations: [
    AppComponent,
    ChartsComponent,
    TeamComponent
  ],
  providers: [
    {
      provide: APP_BASE_HREF,
      useValue: environment.baseHref
    },
    {
      provide: HighchartsStatic,
      useFactory: highchartsFactory
    },
    HabaramaService
  ],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule { }
