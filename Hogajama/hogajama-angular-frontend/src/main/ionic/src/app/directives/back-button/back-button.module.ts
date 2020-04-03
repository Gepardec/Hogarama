import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BackButtonDirective } from './back-button.directive';


@NgModule({
    imports: [
        CommonModule
    ],
    exports: [ BackButtonDirective ],
    declarations: [
        BackButtonDirective
    ]
})
export class BackButtonModule {
}
