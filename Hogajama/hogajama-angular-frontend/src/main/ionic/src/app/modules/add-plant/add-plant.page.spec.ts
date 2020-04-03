import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddPlantPage } from './add-plant.page';

describe('AddPlantPage', () => {
  let component: AddPlantPage;
  let fixture: ComponentFixture<AddPlantPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddPlantPage ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddPlantPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
