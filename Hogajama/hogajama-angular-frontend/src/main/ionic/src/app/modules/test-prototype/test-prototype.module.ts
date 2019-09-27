import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {TestPrototypeRoutingModule} from './test-prototype-routing.module';
import {TestPrototypeComponent} from './test-prototype.component';
import {SharedModule} from "../shared/shared.module";
import {IonicModule} from "@ionic/angular";

@NgModule({
  declarations: [
    TestPrototypeComponent
  ],
    imports: [
        CommonModule,
        TestPrototypeRoutingModule,
        SharedModule,
        IonicModule,
    ]
})
export class TestPrototypeModule { }
