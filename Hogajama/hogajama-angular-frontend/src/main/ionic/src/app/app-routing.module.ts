import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthGuard} from './shared/auth/auth.guard';

const routes: Routes = [
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full'
  },
  {
    path: 'home',
    loadChildren: './modules/test-prototype/test-prototype.module#TestPrototypeModule',
    canLoad: [AuthGuard]
  },
  {
    path: 'testLoginRedirect',
    loadChildren: './modules/test-login-redirect/test-login-redirect.module#TestLoginRedirectModule',
  },
  {
    path: 'playground',
    loadChildren: './modules/testing-playground/testing-playground.module#TestingPlaygroundPageModule',
  },
  { path: 'add-plant', loadChildren: './modules/add-plant/add-plant.module#AddPlantPageModule' },
  { path: 'config-start-info', loadChildren: './modules/config-start-info/config-start-info.module#ConfigStartInfoPageModule' },
  { path: 'config-connect-to-device-info', loadChildren: './modules/config-connect-to-device-info/config-connect-to-device-info.module#ConfigConnectToDeviceInfoPageModule' },
  { path: 'config-connect-device-to-wifi', loadChildren: './modules/config-connect-device-to-wifi/config-connect-device-to-wifi.module#ConfigConnectDeviceToWifiPageModule' },
  { path: 'config-device-setup-complete', loadChildren: './modules/config-device-setup-complete/config-device-setup-complete.module#ConfigDeviceSetupCompletePageModule' }

];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, { useHash: true, initialNavigation: false })
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {}
