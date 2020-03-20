import {NgModule} from '@angular/core';
import {UserNameComponent} from "./user-name/user-name.component";
import {IonicModule} from "@ionic/angular";

@NgModule({
  declarations: [
    UserNameComponent
  ],
    imports: [
        IonicModule
    ],
  exports: [UserNameComponent]
})
export class SharedModule {
}
