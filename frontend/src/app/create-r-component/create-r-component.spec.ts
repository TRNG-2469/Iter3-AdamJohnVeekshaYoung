import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateRComponent } from './create-r-component';

describe('CreateRComponent', () => {
  let component: CreateRComponent;
  let fixture: ComponentFixture<CreateRComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateRComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(CreateRComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
