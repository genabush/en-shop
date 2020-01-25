import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ReactiveFormsModule } from '@angular/forms';

// vendor
import { NgbModalModule } from '@ng-bootstrap/ng-bootstrap';

// components
import { CartAppliedCouponsComponent } from './cart-applied-coupons.component';
import { MockCxIconComponent, MockStore } from 'src/app/testing/mock.components';

// pipes
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { Store } from '@ngrx/store';

// services
import { OccService } from 'src/app/services/occ/occ.service';
import { MockOccService } from 'src/app/testing/occ/occ.service.mock';
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';
import { MockCustomCartService, MockCustomCartVoucherService } from 'src/app/testing/mock.services';
import { CustomCartVoucherService } from 'src/app/services/custom-cart-voucher/custom-cart-voucher.service';

const CART_TEST_VOUCHERS = [{ voucherCode: 'test_coupon' }];

describe('CartAppliedCouponsComponent', () => {
  let component: CartAppliedCouponsComponent;
  let fixture: ComponentFixture<CartAppliedCouponsComponent>;
  let removeModalSpy: jasmine.Spy;
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ReactiveFormsModule, NgbModalModule],
      declarations: [CartAppliedCouponsComponent, MockCxTranslatePipe, MockCxIconComponent],
      providers: [
        { provide: Store, useClass: MockStore },
        { provide: OccService, useClas: MockOccService },
        { provide: CustomCartService, useClass: MockCustomCartService },
        { provide: CustomCartVoucherService, useClass: MockCustomCartVoucherService }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CartAppliedCouponsComponent);
    component = fixture.componentInstance;
    component.vouchers = CART_TEST_VOUCHERS;
    removeModalSpy = spyOn(component, 'openRemovalModal').and.callFake(() => {});
    fixture.detectChanges();
  });
  describe('Component testing', () => {
    it('should CREATE', () => {
      expect(component).toBeTruthy();
    });
  });
  describe('UI testing', () => {
    it('should SHOW an applied coupon', async(() => {
      fixture.whenStable().then(() => {
        const voucherEl = fixture.debugElement.query(By.css('.cx-cart-coupon-code'));
        expect(voucherEl.nativeElement.textContent).toContain(CART_TEST_VOUCHERS[0].voucherCode);
      });
    }));
    it('should SHOW the Remove Modal when an Applied Coupon row `x` is CLICKED', async(() => {
      const removeButton = fixture.debugElement.queryAll(By.css('.close.link'))[0];
      removeButton.triggerEventHandler('click', null);
      fixture.whenStable().then(() => {
        expect(removeModalSpy).toHaveBeenCalledWith(CART_TEST_VOUCHERS[0].voucherCode);
      });
    }));
    it('should CLOSE the Remove Modal when the `Cancel` Button is CLICKED', async(() => {
      removeModalSpy.and.callThrough();
      const closeRemoveSpy = spyOn(component, 'closeRemovalModal').and.callThrough();
      const removeButton = fixture.debugElement.queryAll(By.css('.close.link'))[0];
      removeButton.triggerEventHandler('click', null);
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        document.querySelector('#remove-voucher-form .btn--secondary').dispatchEvent(new Event('click', null));
        fixture.detectChanges();
        fixture.whenStable().then(() => {
          expect(closeRemoveSpy).toHaveBeenCalled();
        });
      });
    }));
    it('should EMIT when `Remove` CLICKED in the Remove Modal', async(() => {
      removeModalSpy.and.callThrough();
      const emitRemoveSpy = spyOn(component.removeVoucherEmit, 'emit');
      const removeButton = fixture.debugElement.queryAll(By.css('.close.link'))[0];
      removeButton.triggerEventHandler('click', null);
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        const fakeEvent: any = { preventDefault: (): any => console.log('preventDefault') };
        document.querySelector('#remove-voucher-form').dispatchEvent(new Event('submit', fakeEvent));
        fixture.detectChanges();
        fixture.whenStable().then(() => {
          expect(emitRemoveSpy).toHaveBeenCalled();
        });
      });
    }));
  });
  afterEach(() => {
    component.ngOnDestroy();
    fixture.destroy();
  });
});
