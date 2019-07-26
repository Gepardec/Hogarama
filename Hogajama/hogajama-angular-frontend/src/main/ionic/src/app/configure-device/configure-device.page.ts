import { Component, OnInit } from '@angular/core';
import { ModalController } from '@ionic/angular';
import { InAppBrowser } from '@ionic-native/in-app-browser/ngx';
import { Network } from '@ionic-native/network/ngx';
import { NetworkInterface } from '@ionic-native/network-interface/ngx';

@Component({
  selector: 'app-configure-device',
  templateUrl: './configure-device.page.html',
  styleUrls: ['./configure-device.page.scss'],
})
export class ConfigureDevicePage implements OnInit {
  public slideOpts = {
    initialSlide: 0,
    speed: 400
  };
  public arduinoIp = '';
  public arduinoPort = '80';

  constructor(public modalController: ModalController, private iab: InAppBrowser, private network: NetworkInterface) { }

  ngOnInit() {
    this.network.getWiFiIPAddress().then(address => {
      this.arduinoIp = address.ip;
    })
    .catch(error => console.error(`Unable to get IP: ${error}`));
  }

  public dismissModal() {
    this.modalController.dismiss();
  }

  public showInAppBrowser() {
    const browser = this.iab.create(`http://${this.arduinoIp}:${this.arduinoPort}/`, '_blank');
  }

}
