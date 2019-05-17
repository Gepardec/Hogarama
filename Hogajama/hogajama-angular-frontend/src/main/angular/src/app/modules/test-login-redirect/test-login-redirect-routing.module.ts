import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { TestLoginRedirectComponent } from './test-login-redirect.component';

const routes: Routes = [
  {
    path: '',
    component: TestLoginRedirectComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TestLoginRedirectRoutingModule { }
