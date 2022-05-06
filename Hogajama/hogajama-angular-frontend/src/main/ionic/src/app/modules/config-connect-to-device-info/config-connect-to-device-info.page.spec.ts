import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfigConnectToDeviceInfoPage } from './config-connect-to-device-info.page';

describe('ConfigConnectToDeviceInfoPage', () => {
  let component: ConfigConnectToDeviceInfoPage;
  let fixture: ComponentFixture<ConfigConnectToDeviceInfoPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfigConnectToDeviceInfoPage ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfigConnectToDeviceInfoPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
