import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {ChatRoutingModule} from './chat-routing.module';
import {ChatComponent} from './chat.component';
import {IonicModule} from '@ionic/angular';
import {FormsModule} from '@angular/forms';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';
import {MatButtonModule} from '@angular/material/button';
import {MatInputModule} from '@angular/material/input'
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {MatIconModule} from '@angular/material/icon';
import {MatDividerModule} from '@angular/material/divider';
import {MatProgressBarModule} from '@angular/material/progress-bar';
@NgModule({
  declarations: [ChatComponent],
    imports: [
        CommonModule,
        ChatRoutingModule,
        IonicModule,
        MatCardModule,
        FormsModule,
        MatFormFieldModule,
        MatSlideToggleModule,
        MatButtonModule,
        MatInputModule,
        MatProgressSpinnerModule,
        MatIconModule,
        MatDividerModule,
        MatProgressBarModule
    ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ChatModule { }
