import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {LoginRoutingModule} from './login-routing.module';
import {LoginComponent} from './login.component';
import {IonicModule} from '@ionic/angular';
import { MatButtonModule } from '@angular/material/button';

@NgModule({
  declarations: [LoginComponent],
    imports: [
        CommonModule,
        LoginRoutingModule,
        IonicModule,
        MatButtonModule
    ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LoginModule { }
