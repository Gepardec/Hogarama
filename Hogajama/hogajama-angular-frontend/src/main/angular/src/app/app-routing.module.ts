import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import { TestPrototypeComponent } from './modules/test-prototype/test-prototype.component';
import { TestPrototypeModule } from './modules/test-prototype/test-prototype.module';

const routes: Routes = [
  {
    path: '',
    redirectTo: '/testPrototype',
    pathMatch: 'full'
  },
  {
    path: 'testPrototype',
    loadChildren: './modules/test-prototype/test-prototype.module#TestPrototypeModule',
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
