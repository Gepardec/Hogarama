import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { TestingPlaygroundPage } from './testing-playground.page';

const routes: Routes = [
  {
    path: '',
    component: TestingPlaygroundPage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TestingPlaygroundRoutingModule { }
