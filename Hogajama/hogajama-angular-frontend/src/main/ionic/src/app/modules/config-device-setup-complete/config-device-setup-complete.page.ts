import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-config-device-setup-complete',
  templateUrl: './config-device-setup-complete.page.html',
  styleUrls: ['./config-device-setup-complete.page.scss'],
})
export class ConfigDeviceSetupCompletePage implements OnInit {

  constructor(
    private router: Router
  ) { }

  ngOnInit() {
  }

  backToHome() {
    this.router.navigateByUrl('home');
  }
}
