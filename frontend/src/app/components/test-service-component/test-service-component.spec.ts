import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TestServiceComponent } from './test-service-component';

describe('TestServiceComponent', () => {
  let component: TestServiceComponent;
  let fixture: ComponentFixture<TestServiceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TestServiceComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(TestServiceComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
