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
  }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, { useHash: true, initialNavigation: false })
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {}
