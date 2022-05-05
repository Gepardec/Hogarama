import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { MatDialogModule } from '@angular/material/dialog';
import { waitForAsync, ComponentFixture, TestBed } from '@angular/core/testing';

import { TestingPlaygroundPage } from './testing-playground.page';

describe('TestingPlaygroundPage', () => {
  let component: TestingPlaygroundPage;
  let fixture: ComponentFixture<TestingPlaygroundPage>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule, MatDialogModule
      ],
      declarations: [ TestingPlaygroundPage ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TestingPlaygroundPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
