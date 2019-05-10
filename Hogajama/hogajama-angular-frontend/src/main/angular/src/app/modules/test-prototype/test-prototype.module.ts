import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TestPrototypeRoutingModule } from './test-prototype-routing.module';
import { TestPrototypeComponent } from './test-prototype.component';

@NgModule({
  declarations: [
    TestPrototypeComponent
  ],
  imports: [
    CommonModule,
    TestPrototypeRoutingModule
  ]
})
export class TestPrototypeModule { }
