import { Directive, HostListener } from '@angular/core';
import { NavController, Platform } from '@ionic/angular';
import { Location } from "@angular/common";

@Directive({
  selector: '[backButton]'
})
export class BackButtonDirective {
  constructor(
    private location: Location,
    private nav: NavController,
    private platform: Platform
) {
}

@HostListener('click')
onClick() {
    if (this.platform.is('cordova')) {
        this.location.back();
    }
    else {
        this.nav.back();
    }
}

}
