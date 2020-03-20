import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-config-connect-to-device-info',
  templateUrl: './config-connect-to-device-info.page.html',
  styleUrls: ['./config-connect-to-device-info.page.scss'],
})
export class ConfigConnectToDeviceInfoPage implements OnInit {

  constructor(
    private router: Router
  ) { }

  ngOnInit() {
  }

  nextStep() {
    this.router.navigateByUrl('config-connect-device-to-wifi');
  }
}
