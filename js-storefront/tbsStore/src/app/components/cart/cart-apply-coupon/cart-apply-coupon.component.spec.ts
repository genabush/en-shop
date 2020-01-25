import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientTestingModule } from '@angular/common/http/testing';

//spartacus
import { GlobalMessageService } from '@spartacus/core';

// vendor
import { MockComponent } from 'ng-mocks';
import { Store } from '@ngrx/store';
import { MatInputModule } from '@angular/material';

// components
import { CartApplyCouponComponent } from './cart-apply-coupon.component';
import { CartAppliedCouponsComponent } from './cart-applied-coupons/cart-applied-coupons.component';

import { MockStore, MockCxIconComponent } from 'src/app/testing/mock.components';

// services
import { OccService } from 'src/app/services/occ/occ.service';
import { MockOccService } from 'src/app/testing/occ/occ.service.mock';
import {
  MockCustomCartService,
  MockGlobalMessagingService,
  MockCustomCartVoucherService
} from 'src/app/testing/mock.services';
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';

// pipes
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { CustomCartVoucherService } from 'src/app/services/custom-cart-voucher/custom-cart-voucher.service';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

describe('CartApplyCouponComponent', () => {
  let component: CartApplyCouponComponent;
  let fixture: ComponentFixture<CartApplyCouponComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, BrowserAnimationsModule, ReactiveFormsModule, MatInputModule],
      declarations: [
        CartApplyCouponComponent,
        MockComponent(CartAppliedCouponsComponent),
        MockCxTranslatePipe,
        MockCxIconComponent
      ],
      providers: [
        { provide: Store, useClass: MockStore },
        { provide: OccService, useClas: MockOccService },
        { provide: CustomCartService, useClass: MockCustomCartService },
        { provide: CustomCartVoucherService, useClass: MockCustomCartVoucherService },
        { provide: GlobalMessageService, useClass: MockGlobalMessagingService }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CartApplyCouponComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });
  describe('Component testing', () => {
    it('should CREATE', () => {
      expect(component).toBeTruthy();
    });
  });
  describe('UI testing', () => {
    it('should show a `Disabled` Apply Button when the input field is empty', async(() => {
      const couponInput: DebugElement = fixture.debugElement.query(By.css('.input-coupon-code'));
      couponInput.nativeElement.value = null;
      couponInput.nativeElement.dispatchEvent(new Event('input'));
      // change input field
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        const submitBtn = fixture.debugElement.query(By.css('.apply-coupon-button'));
        expect(submitBtn.nativeElement.disabled).toBeTruthy();
      });
    }));
    it('should show an `Enabled` Apply Button when the input field is empty', async(() => {
      const couponInput: DebugElement = fixture.debugElement.query(By.css('.input-coupon-code'));
      couponInput.nativeElement.value = 'test';
      couponInput.nativeElement.dispatchEvent(new Event('input'));
      // change input field
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        const submitBtn = fixture.debugElement.query(By.css('.apply-coupon-button'));
        expect(submitBtn.nativeElement.disabled).toBeFalsy();
      });
    }));
  });
  afterEach(() => {
    component.ngOnDestroy();
    fixture.destroy();
  });
});
