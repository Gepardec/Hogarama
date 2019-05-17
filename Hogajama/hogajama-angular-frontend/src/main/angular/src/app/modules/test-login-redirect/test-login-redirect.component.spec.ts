import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TestLoginRedirectComponent } from './test-login-redirect.component';

describe('TestLoginRedirectComponent', () => {
  let component: TestLoginRedirectComponent;
  let fixture: ComponentFixture<TestLoginRedirectComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TestLoginRedirectComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TestLoginRedirectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
