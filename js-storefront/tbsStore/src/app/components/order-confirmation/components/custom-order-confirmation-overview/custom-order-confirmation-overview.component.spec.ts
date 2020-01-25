import { Component, Input } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { CheckoutService, I18nTestingModule, Order } from '@spartacus/core';
import { Observable, of } from 'rxjs';

import createSpy = jasmine.createSpy;
import { Card } from '@spartacus/storefront';
import { CustomOrderConfirmationOverviewComponent } from './custom-order-confirmation-overview.component';

@Component({ selector: 'cx-card', template: '' })
class MockCardComponent {
  @Input()
  content: Card;
}

class MockCheckoutService {
  clearCheckoutData = createSpy();

  getOrderDetails(): Observable<Order> {
    return of({
      code: 'test-code-412',
      deliveryAddress: {
        country: {}
      },
      deliveryMode: {},
      paymentInfo: {
        billingAddress: {
          country: {}
        }
      }
    });
  }
}

describe('OrderConfirmationOverviewComponent', () => {
  let component: CustomOrderConfirmationOverviewComponent;
  let fixture: ComponentFixture<CustomOrderConfirmationOverviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [I18nTestingModule],
      declarations: [CustomOrderConfirmationOverviewComponent, MockCardComponent],
      providers: [{ provide: CheckoutService, useClass: MockCheckoutService }]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomOrderConfirmationOverviewComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    component.ngOnInit();
    expect(component).toBeTruthy();
  });
});
