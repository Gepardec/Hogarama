import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TestLoginRedirectRoutingModule } from './test-login-redirect-routing.module';
import { TestLoginRedirectComponent } from './test-login-redirect.component';

@NgModule({
  declarations: [TestLoginRedirectComponent],
  imports: [
    CommonModule,
    TestLoginRedirectRoutingModule
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TestLoginRedirectModule { }
