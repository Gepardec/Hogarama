import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfigDeviceSetupCompletePage } from './config-device-setup-complete.page';

describe('ConfigDeviceSetupCompletePage', () => {
  let component: ConfigDeviceSetupCompletePage;
  let fixture: ComponentFixture<ConfigDeviceSetupCompletePage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfigDeviceSetupCompletePage ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfigDeviceSetupCompletePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
