import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import {
  CheckoutDeliveryService,
  UserPaymentService,
  RoutingService,
  BaseSiteService,
  OccConfig,
  CurrencyService,
  CartDataService
} from '@spartacus/core';
import { of, BehaviorSubject, Observable, from, EMPTY } from 'rxjs';
import { ReactiveFormsModule } from '@angular/forms';
import { CustomCheckoutService } from '../../../../services/checkout/custom-checkout.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ActivatedRoute } from '@angular/router';

// spartacus
import { CheckoutConfigService, CardModule, SpinnerModule, GenericLinkModule, Card } from '@spartacus/storefront';

//vendor
import { Store } from '@ngrx/store';
import { MatCheckboxModule, MatRadioModule } from '@angular/material';

// components
import { CustomCheckoutPaymentDetailsComponent } from './custom-checkout-payment-details.component';
import { OrderSummaryComponent } from 'src/app/components/cart-shared/order-summary/order-summary.component';
import { CustomCheckoutAddressCardsComponent } from '../../components/custom-checkout-address-cards/custom-checkout-address-cards.component';
import { CustomCheckoutAddressFormComponent } from '../../components/custom-checkout-address-form/custom-checkout-address-form.component';
import { MockCxIconComponent, MockPaypalComponent } from 'src/app/testing/mock.components';
import { GiftCardFormComponent } from '../gift-card-form/gift-card-form.component';
import { GiftCardsComponent } from '../gift-cards/gift-cards.component';
import { CustomCheckoutDeliveryAddressComponent } from '../../fulfillment/custom-checkout-delivery-address/custom-checkout-delivery-address.component';
import { CheckoutPaymentAddressComponent } from '../checkout-payment-address/checkout-payment-address.component';
import { PaymentMethodAccordionComponent } from '../payment-method-accordion/payment-method-accordion.component';
import { AdyenSavedCreditCardsComponent } from '../adyen-saved-credit-cards/adyen-saved-credit-cards.component';
import { AdyenCreditCardsComponent } from '../adyen-credit-cards/adyen-credit-cards.component';
import { AdyenGiropayComponent } from '../adyen-giropay/adyen-giropay.component';
import { AdyenIdealComponent } from '../adyen-ideal/adyen-ideal.component';

import { MockComponent, MockPipe } from 'ng-mocks';

// services
import {
  MockCheckoutConfigService,
  MockCustomCartService,
  MockRoutingService,
  MockUserPaymentService,
  MockCustomCheckoutService,
  MockBaseSiteService,
  MockCartDataService
} from 'src/app/testing/mock.services';
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';

// pipes
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { AddressFormShowFieldPipe } from 'src/app/pipes/address-form-showfield/address-form-show-field.pipe';
import { AddressSelectCardPipe } from 'src/app/pipes/address-select-card/address-select-card.pipe';
import { PayNowEnabledPipe } from 'src/app/pipes/pay-now-enabled/pay-now-enabled.pipe';
import { LoyaltyVoucherListComponent } from 'src/app/components/checkout/payments/loyalty-voucher-list/loyalty-voucher-list.component';
import { MockOccConfig } from 'src/app/testing/occ/occ.config.mock';
import { NgbAccordionModule } from '@ng-bootstrap/ng-bootstrap';
import { CheckoutBasketSummaryComponent } from '../../components/checkout-basket-summary/checkout-basket-summary.component';

class MockCheckoutDeliveryService {
  getDeliveryAddress() {
    return of({ region: { iscode: '123' }, lastName: 'test', country: { iscode: 456 } });
  }
}

class MockStore<T> {
  private state: BehaviorSubject<T> = new BehaviorSubject(undefined);
  setState(data: T) {
    this.state.next(data);
  }
  select(selector?: any): Observable<T> {
    return this.state.asObservable();
  }
  dispatch(action: any) {}
  pipe(func) {
    return of({});
  }
}

class MockCurrencyService {
  getActive() {
    return EMPTY;
  }
}

describe('CustomCheckoutPaymentDetailsComponent', () => {
  let component: CustomCheckoutPaymentDetailsComponent;
  let fixture: ComponentFixture<CustomCheckoutPaymentDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        ReactiveFormsModule,
        RouterTestingModule,
        MatCheckboxModule,
        MatRadioModule,
        NgbAccordionModule
      ],
      declarations: [
        CustomCheckoutPaymentDetailsComponent,
        MockComponent(CustomCheckoutAddressCardsComponent),
        MockComponent(CustomCheckoutAddressFormComponent),
        MockComponent(CheckoutPaymentAddressComponent),
        MockComponent(CustomCheckoutDeliveryAddressComponent),
        MockComponent(PaymentMethodAccordionComponent),
        MockComponent(OrderSummaryComponent),
        MockComponent(AdyenCreditCardsComponent),
        MockComponent(AdyenSavedCreditCardsComponent),
        MockComponent(AdyenGiropayComponent),
        MockComponent(AdyenIdealComponent),
        MockComponent(GiftCardsComponent),
        MockComponent(GiftCardFormComponent),
        MockComponent(LoyaltyVoucherListComponent),
        MockComponent(CheckoutBasketSummaryComponent),
        MockCxTranslatePipe,
        MockCxIconComponent,
        MockPaypalComponent,
        MockPipe(AddressSelectCardPipe, () => ({} as Card)),
        MockPipe(AddressFormShowFieldPipe, () => true),
        MockPipe(PayNowEnabledPipe, () => true)
      ],
      providers: [
        { provide: Store, useClass: MockStore },
        { provide: CheckoutDeliveryService, useClass: MockCheckoutDeliveryService },
        { provide: UserPaymentService, useClass: MockUserPaymentService },
        { provide: CustomCheckoutService, useClass: MockCustomCheckoutService },
        { provide: CheckoutConfigService, useClass: MockCheckoutConfigService },
        { provide: RoutingService, useClass: MockRoutingService },
        { provide: CustomCartService, useClass: MockCustomCartService },
        { provide: CartDataService, useClass: MockCartDataService },
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{ id: 1 }])
          }
        },
        { provide: BaseSiteService, useClass: MockBaseSiteService },
        { provide: OccConfig, useClass: MockOccConfig },
        { provide: CurrencyService, useClass: MockCurrencyService }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomCheckoutPaymentDetailsComponent);
    component = fixture.componentInstance;
    component.checkoutState = {
      fullFillmentState: false,
      deliveryModesState: false,
      whoWillCollectState: false,
      paymentDetailsState: false
    };
    spyOn(component, 'watchTriggerToCloseModal').and.callFake(() => {});
    fixture.detectChanges();
  });

  it('should CREATE', () => {
    expect(component).toBeTruthy();
  });
});
