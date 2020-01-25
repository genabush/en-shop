import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LoyaltyVoucherListComponent } from './loyalty-voucher-list.component';
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { MockDatePipe } from '@spartacus/core';
import { MockComponent } from 'ng-mocks';
import { LoyaltyVoucherComponent } from 'src/app/components/checkout/payments/loyalty-voucher/loyalty-voucher.component';
import { CustomCheckoutService } from 'src/app/services/checkout/custom-checkout.service';
import { MockCustomCheckoutService } from 'src/app/testing/mock.services';

describe('LoyaltyVoucherListComponent', () => {
  let component: LoyaltyVoucherListComponent;
  let fixture: ComponentFixture<LoyaltyVoucherListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        LoyaltyVoucherListComponent,
        MockCxTranslatePipe,
        MockDatePipe,
        MockComponent(LoyaltyVoucherComponent)
      ],
      providers: [{ provide: CustomCheckoutService, useClass: MockCustomCheckoutService }]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoyaltyVoucherListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
