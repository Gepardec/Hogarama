import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import { AuthGuard } from './shared/auth/auth.guard';

const routes: Routes = [
  {
    path: '',
    redirectTo: '/testPrototype',
    pathMatch: 'full'
  },
  {
    path: 'testPrototype',
    loadChildren: './modules/test-prototype/test-prototype.module#TestPrototypeModule',
    canLoad: [AuthGuard]
  },
  {
    path: 'testLoginRedirect',
    loadChildren: './modules/test-login-redirect/test-login-redirect.module#TestLoginRedirectModule',
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
