import { Component, Input } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { Cart, CheckoutService, I18nTestingModule, Order } from '@spartacus/core';
import { Observable, of } from 'rxjs';

import createSpy = jasmine.createSpy;
import { By } from '@angular/platform-browser';
import { CustomOrderConfirmationTotalsComponent } from './custom-order-confirmation-totals.component';

@Component({ selector: 'cx-order-summary', template: '' })
class MockOrderSummaryComponent {
  @Input()
  cart: Cart;
}

class MockCheckoutService {
  clearCheckoutData = createSpy();

  getOrderDetails(): Observable<Order> {
    return of({
      code: 'test-code-412'
    });
  }
}

describe('Order Confirmation Totals Component', () => {
  let component: CustomOrderConfirmationTotalsComponent;
  let fixture: ComponentFixture<CustomOrderConfirmationTotalsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [I18nTestingModule],
      declarations: [CustomOrderConfirmationTotalsComponent, MockOrderSummaryComponent],
      providers: [{ provide: CheckoutService, useClass: MockCheckoutService }]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomOrderConfirmationTotalsComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    component.ngOnInit();
    expect(component).toBeTruthy();
  });
});
