import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LoyaltyVoucherComponent } from './loyalty-voucher.component';
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { MockDatePipe } from '@spartacus/core';

describe('LoyaltyVoucherComponent', () => {
  let component: LoyaltyVoucherComponent;
  let fixture: ComponentFixture<LoyaltyVoucherComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [LoyaltyVoucherComponent, MockCxTranslatePipe, MockDatePipe]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoyaltyVoucherComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
