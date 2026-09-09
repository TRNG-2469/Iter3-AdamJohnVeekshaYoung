import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReimbursementsScreen } from './reimbursements-screen';

describe('ReimbursementsScreen', () => {
  let component: ReimbursementsScreen;
  let fixture: ComponentFixture<ReimbursementsScreen>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReimbursementsScreen],
    }).compileComponents();

    fixture = TestBed.createComponent(ReimbursementsScreen);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
