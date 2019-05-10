import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TestPrototypeComponent } from './test-prototype.component';

describe('TestPrototypeComponent', () => {
  let component: TestPrototypeComponent;
  let fixture: ComponentFixture<TestPrototypeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TestPrototypeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TestPrototypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
