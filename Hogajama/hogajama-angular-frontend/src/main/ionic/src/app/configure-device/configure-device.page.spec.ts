import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfigureDevicePage } from './configure-device.page';

describe('ConfigureDevicePage', () => {
  let component: ConfigureDevicePage;
  let fixture: ComponentFixture<ConfigureDevicePage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfigureDevicePage ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfigureDevicePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
