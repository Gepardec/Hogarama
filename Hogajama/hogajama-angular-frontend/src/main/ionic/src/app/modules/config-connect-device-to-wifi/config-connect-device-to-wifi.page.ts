import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-config-connect-device-to-wifi',
  templateUrl: './config-connect-device-to-wifi.page.html',
  styleUrls: ['./config-connect-device-to-wifi.page.scss'],
})
export class ConfigConnectDeviceToWifiPage implements OnInit {
  wifis: {name: string, ssid: string}[] = [];


  constructor(
    private router: Router
  ) { }

  ngOnInit() {
  }

  nextStep() {
    this.router.navigateByUrl('config-device-setup-complete');
  }

}
