import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {TestLoginRedirectRoutingModule} from './test-login-redirect-routing.module';
import {TestLoginRedirectComponent} from './test-login-redirect.component';
import {IonicModule} from "@ionic/angular";

@NgModule({
  declarations: [TestLoginRedirectComponent],
    imports: [
        CommonModule,
        TestLoginRedirectRoutingModule,
        IonicModule
    ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TestLoginRedirectModule { }
