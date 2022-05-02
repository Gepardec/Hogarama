import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {RouteReuseStrategy} from '@angular/router';

import {IonicModule, IonicRouteStrategy} from '@ionic/angular';
import {SplashScreen} from '@ionic-native/splash-screen/ngx';
import {StatusBar} from '@ionic-native/status-bar/ngx';

import {AppComponent} from './app.component';
import {AppRoutingModule} from './app-routing.module';
import {NetworkInterface} from '@ionic-native/network-interface/ngx';
import {InAppBrowser} from '@ionic-native/in-app-browser/ngx';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {DeviceDetectorModule} from 'ngx-device-detector';
import { IonicStorageModule } from '@ionic/storage-angular';
import {MyHttpInterceptor} from './services/HttpInterceptor/http-interceptor';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SideMenuComponent } from './modules/shared/side-menu/side-menu.component';

@NgModule({
  declarations: [AppComponent, SideMenuComponent],
  entryComponents: [
  ],
  imports: [
    BrowserModule,
    IonicModule.forRoot(),
    AppRoutingModule,
    HttpClientModule,
    DeviceDetectorModule.forRoot(),
    IonicStorageModule.forRoot(),
    BrowserAnimationsModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: MyHttpInterceptor,
      multi: true
    },
    StatusBar,
    SplashScreen,
    InAppBrowser,
    NetworkInterface,
    { provide: RouteReuseStrategy, useClass: IonicRouteStrategy }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
