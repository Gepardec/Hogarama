import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfigConnectDeviceToWifiPage } from './config-connect-device-to-wifi.page';

describe('ConfigConnectDeviceToWifiPage', () => {
  let component: ConfigConnectDeviceToWifiPage;
  let fixture: ComponentFixture<ConfigConnectDeviceToWifiPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfigConnectDeviceToWifiPage ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfigConnectDeviceToWifiPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
