import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';

// spartacus
import {
  CartDataService,
  OccConfig,
  BaseSiteService,
  RoutingConfig,
  CartService,
  TranslationService
} from '@spartacus/core';
import { Card } from '@spartacus/storefront';

// vendor
import { Store } from '@ngrx/store';
import { MockComponent, MockPipe } from 'ng-mocks';

// components
import { CustomCheckoutOrchestratorComponent } from './checkout-orchestrator.component';
import { CheckoutFulfillmentNavTabsComponent } from '../../fulfillment/checkout-fulfillment-nav-tabs/checkout-fulfillment-nav-tabs.component';
import { CustomCheckoutFulfillmentTabsComponent } from '../../fulfillment/checkout-fullfillment-tabs/checkout-fulfillment-tabs.component';
import { CheckoutDeliveryErrorsComponent } from '../../fulfillment/checkout-delivery-errors/checkout-delivery-errors.component';
import { CustomCheckoutDeliveryAddressComponent } from '../../fulfillment/custom-checkout-delivery-address/custom-checkout-delivery-address.component';
import { CustomDeliveryModeComponent } from '../../fulfillment/custom-checkout-delivery-mode/custom-checkout-delivery-mode.component';
import { CustomCheckoutDeliveryModesCardsComponent } from '../../fulfillment/custom-checkout-delivery-modes-cards/custom-checkout-delivery-modes-cards.component';
import { CustomCheckoutPaymentDetailsComponent } from '../../payments/custom-checkout-payment-details/custom-checkout-payment-details.component';
import { CheckoutPaymentAddressComponent } from '../../payments/checkout-payment-address/checkout-payment-address.component';
import { MockCxGenericLinkComponent, MockCxIconComponent, MockStore } from 'src/app/testing/mock.components';
import { CustomCheckoutAddressCardsComponent } from '../custom-checkout-address-cards/custom-checkout-address-cards.component';
import { CustomCheckoutAddressFormComponent } from '../custom-checkout-address-form/custom-checkout-address-form.component';
import { CustomCheckoutCollectionPointComponent } from '../../fulfillment/custom-checkout-collection-point/custom-checkout-collection-point.component';
import { GiftCardsComponent } from '../../payments/gift-cards/gift-cards.component';
import { GiftCardFormComponent } from '../../payments/gift-card-form/gift-card-form.component';
import { PaymentMethodAccordionComponent } from '../../payments/payment-method-accordion/payment-method-accordion.component';
import { AdyenCreditCardsComponent } from '../../payments/adyen-credit-cards/adyen-credit-cards.component';
import { AdyenSavedCreditCardsComponent } from '../../payments/adyen-saved-credit-cards/adyen-saved-credit-cards.component';
import { PaypalComponent } from '../../payments/paypal/paypal.component';
import { CustomAddressFinderComponent } from '../../fulfillment/custom-address-finder/custom-address-finder.component';
import { WhoWillCollectComponent } from '../../fulfillment/who-will-collect/who-will-collect.component';
import { CollectionPointItemComponent } from '../../fulfillment/collection-point-item/collection-point-item.component';
import { MapInfoWindowComponent } from '../map-info-window/map-info-window.component';
import { MapInfoWindowAltComponent } from '../map-info-window-alt/map-info-window-alt.component';
import { CustomCheckoutCollectInStoreComponent } from '../../fulfillment/custom-checkout-collect-in-store/custom-checkout-collect-in-store.component';
import { AdyenIdealComponent } from '../../payments/adyen-ideal/adyen-ideal.component';
import { CustomCisFinderComponent } from '../../fulfillment/custom-cis-finder/custom-cis-finder.component';
import { AdyenGiropayComponent } from '../../payments/adyen-giropay/adyen-giropay.component';
import { OrderSummaryComponent } from 'src/app/components/cart-shared/order-summary/order-summary.component';

// services
import {
  MockCartDataService,
  MockBaseSiteService,
  MockCustomCartService,
  MockCartService,
  MockTranslationService
} from 'src/app/testing/mock.services';
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';

// pipes
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { AddressFormShowFieldPipe } from 'src/app/pipes/address-form-showfield/address-form-show-field.pipe';
import { AddressSelectCardPipe } from 'src/app/pipes/address-select-card/address-select-card.pipe';
import { DeliveryModesSelectedCardPipe } from 'src/app/pipes/delivery-modes-card/delivery-modes-selected-card.pipe';
import { DeliveryModesCardPipe } from 'src/app/pipes/delivery-modes-card/delivery-modes-card.pipe';

import { MockOccConfig } from 'src/app/testing/occ/occ.config.mock';
import { CheckoutBasketSummaryComponent } from '../checkout-basket-summary/checkout-basket-summary.component';

describe('CheckoutOrchestratorComponent', () => {
  let component: CustomCheckoutOrchestratorComponent;
  let fixture: ComponentFixture<CustomCheckoutOrchestratorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule, HttpClientTestingModule, ReactiveFormsModule],
      declarations: [
        CustomCheckoutOrchestratorComponent,
        MockComponent(CheckoutDeliveryErrorsComponent),
        MockComponent(CustomCheckoutDeliveryAddressComponent),
        MockComponent(CustomCheckoutAddressFormComponent),
        MockComponent(CustomCheckoutAddressCardsComponent),
        MockComponent(CustomDeliveryModeComponent),
        MockComponent(CustomCheckoutDeliveryModesCardsComponent),
        MockComponent(CheckoutFulfillmentNavTabsComponent),
        MockComponent(CustomCheckoutFulfillmentTabsComponent),
        MockComponent(CustomCheckoutPaymentDetailsComponent),
        MockComponent(PaymentMethodAccordionComponent),
        MockComponent(CheckoutPaymentAddressComponent),
        MockComponent(CustomCheckoutCollectionPointComponent),
        MockComponent(CustomCheckoutCollectInStoreComponent),
        MockComponent(CustomAddressFinderComponent),
        MockComponent(CustomCisFinderComponent),
        MockComponent(OrderSummaryComponent),
        MockComponent(WhoWillCollectComponent),
        MockComponent(CollectionPointItemComponent),
        MockComponent(MapInfoWindowComponent),
        MockComponent(MapInfoWindowAltComponent),
        MockComponent(AdyenCreditCardsComponent),
        MockComponent(AdyenSavedCreditCardsComponent),
        MockComponent(AdyenIdealComponent),
        MockComponent(AdyenGiropayComponent),
        MockComponent(GiftCardsComponent),
        MockComponent(GiftCardFormComponent),
        MockComponent(PaypalComponent),
        MockComponent(CheckoutBasketSummaryComponent),
        MockCxTranslatePipe,
        MockCxIconComponent,
        MockCxGenericLinkComponent,
        MockPipe(AddressFormShowFieldPipe, () => 'test'),
        MockPipe(AddressSelectCardPipe, () => ({} as Card)),
        MockPipe(DeliveryModesSelectedCardPipe, () => true),
        MockPipe(DeliveryModesCardPipe, () => ({} as Card))
      ],
      providers: [
        { provide: Store, useClass: MockStore },
        { provide: CartDataService, useClass: MockCartDataService },
        { provide: OccConfig, useClass: MockOccConfig },
        { provide: BaseSiteService, useClass: MockBaseSiteService },
        { provide: TranslationService, useClass: MockTranslationService },
        { provide: CustomCartService, useClass: MockCustomCartService },
        { provide: CartService, useClass: MockCartService },
        RoutingConfig
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomCheckoutOrchestratorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should CREATE', () => {
    expect(component).toBeTruthy();
  });
  it('should SHOW a restriction message when an item is RESTRICTED', async(() => {
    component.checkoutJourneyType = 0;
    component.checkoutStates = {
      fullFillmentState: true,
      deliveryModesState: false,
      whoWillCollectState: false,
      paymentDetailsState: false
    };
    component.deliveryModesRestricted = {
      isDeliveryModesRestricted: true,
      errorsList: ['delivery.error.hazmatDeliveryMode']
    };
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      expect(fixture.debugElement.query(By.css('.cx-delivery-errors'))).toBe(null);
    });
  }));
});
