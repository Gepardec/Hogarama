import { Component } from '@angular/core';
import { ModalController } from '@ionic/angular';
import { ConfigureDevicePage } from '../configure-device/configure-device.page';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
export class HomePage {

  constructor(public modalController: ModalController) {}


  async presentConfigureModal() {
    const modal = await this.modalController.create({
      component: ConfigureDevicePage
    });
    return await modal.present();
  }
}
