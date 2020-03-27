import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { TestPrototypeComponent } from './test-prototype.component';

const routes: Routes = [
  {
    path: '',
    component: TestPrototypeComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TestPrototypeRoutingModule { }
