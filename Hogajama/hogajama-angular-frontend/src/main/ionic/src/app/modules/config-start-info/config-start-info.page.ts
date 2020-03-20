import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-config-start-info',
  templateUrl: './config-start-info.page.html',
  styleUrls: ['./config-start-info.page.scss'],
})
export class ConfigStartInfoPage implements OnInit {

  constructor(
    private router: Router
  ) { }

  ngOnInit() {
  }

  nextStep() {
    this.router.navigateByUrl('config-connect-to-device-info');
  }
}
