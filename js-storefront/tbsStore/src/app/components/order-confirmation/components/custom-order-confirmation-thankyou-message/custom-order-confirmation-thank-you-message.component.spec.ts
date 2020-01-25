import { Component, Input, Type } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { CheckoutService, I18nTestingModule, Order } from '@spartacus/core';
import { Observable, of } from 'rxjs';

import createSpy = jasmine.createSpy;
import { CustomOrderConfirmationThankYouMessageComponent } from './custom-order-confirmation-thank-you-message.component';
import { MockComponent } from 'ng-mocks';
import { LybcMessageComponent } from 'src/app/components/lybc-message/lybc-message.component';

@Component({ selector: 'cx-add-to-home-screen-banner', template: '' })
class MockAddtoHomeScreenBannerComponent {}

@Component({ selector: 'cx-guest-register-form', template: '' })
class MockGuestRegisterFormComponent {
  @Input() guid;
  @Input() email;
}

class MockCheckoutService {
  clearCheckoutData = createSpy();
  getOrderDetails(): Observable<Order> {
    return of({
      code: 'test-code-412',
      guid: 'guid',
      guestCustomer: true,
      paymentInfo: { billingAddress: { email: 'test@test.com' } }
    });
  }
}

describe('Order Confirmation Thank You Component', () => {
  let component: CustomOrderConfirmationThankYouMessageComponent;
  let fixture: ComponentFixture<CustomOrderConfirmationThankYouMessageComponent>;

  let checkoutService: CheckoutService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [I18nTestingModule],
      declarations: [
        CustomOrderConfirmationThankYouMessageComponent,
        MockAddtoHomeScreenBannerComponent,
        MockGuestRegisterFormComponent,
        MockComponent(LybcMessageComponent)
      ],
      providers: [{ provide: CheckoutService, useClass: MockCheckoutService }]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomOrderConfirmationThankYouMessageComponent);
    component = fixture.componentInstance;
    checkoutService = TestBed.get(CheckoutService as Type<CheckoutService>);
  });

  it('should create', () => {
    component.ngOnInit();
    expect(component).toBeTruthy();
  });
});
