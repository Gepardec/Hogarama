import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfigStartInfoPage } from './config-start-info.page';

describe('ConfigStartInfoPage', () => {
  let component: ConfigStartInfoPage;
  let fixture: ComponentFixture<ConfigStartInfoPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfigStartInfoPage ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfigStartInfoPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
