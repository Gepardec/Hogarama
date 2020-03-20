import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {TestPrototypeRoutingModule} from './test-prototype-routing.module';
import {TestPrototypeComponent} from './test-prototype.component';
import {SharedModule} from "../shared/shared.module";
import {IonicModule} from "@ionic/angular";
import { MatButtonModule } from '@angular/material';

@NgModule({
  declarations: [
    TestPrototypeComponent
  ],
    imports: [
        CommonModule,
        TestPrototypeRoutingModule,
        SharedModule,
        IonicModule,
        MatButtonModule
    ]
})
export class TestPrototypeModule { }
