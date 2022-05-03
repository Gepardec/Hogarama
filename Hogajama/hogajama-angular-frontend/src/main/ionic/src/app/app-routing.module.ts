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
    loadChildren: () => import('./modules/home/home.module').then(m => m.HomeModule),
    canLoad: [AuthGuard]
  },
  {
    path: 'login',
    loadChildren: () => import('./modules/login/login.module').then(m => m.LoginModule),
  },
  {
    path: 'playground',
    loadChildren: () => import('./modules/testing-playground/testing-playground.module')
        .then(m => m.TestingPlaygroundPageModule)
  },
  {
    path: 'add-plant',
    loadChildren: () => import('./modules/add-plant/add-plant.module')
        .then(m => m.AddPlantPageModule)
  },
  {
    path: 'config-start-info',
    loadChildren: () => import('./modules/config-start-info/config-start-info.module')
        .then(m => m.ConfigStartInfoPageModule)
  },
  {
    path: 'config-connect-to-device-info',
    loadChildren: () => import('./modules/config-connect-to-device-info/config-connect-to-device-info.module')
        .then(m => m.ConfigConnectToDeviceInfoPageModule)
  },
  { path: 'config-connect-device-to-wifi',
    loadChildren: () => import('./modules/config-connect-device-to-wifi/config-connect-device-to-wifi.module')
        .then(m => m.ConfigConnectDeviceToWifiPageModule)
  },
  {
    path: 'config-device-setup-complete',
    loadChildren: () => import('./modules/config-device-setup-complete/config-device-setup-complete.module')
        .then(m => m.ConfigDeviceSetupCompletePageModule)
  }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, { useHash: true, initialNavigation: null })
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {}
