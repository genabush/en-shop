import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatSelectModule, MatInputModule, MatRadioModule, MatIconModule, MatCheckboxModule } from '@angular/material';

// spartacus
import { CartNotEmptyGuard, CardModule, SpinnerModule, GenericLinkModule, IconModule } from '@spartacus/storefront';
import { ConfigModule, AuthGuard, I18nModule } from '@spartacus/core';

// vendor
import { NgxPayPalModule } from 'ngx-paypal';
import { NgbAccordionModule, NgbAlertModule } from '@ng-bootstrap/ng-bootstrap';
import { AgmCoreModule } from '@agm/core';
import { NgbTabsetModule } from '@ng-bootstrap/ng-bootstrap';

// components
import { AdyenCreditCardsComponent } from './payments/adyen-credit-cards/adyen-credit-cards.component';
import { AdyenSavedCreditCardsComponent } from './payments/adyen-saved-credit-cards/adyen-saved-credit-cards.component';
import { GiftCardsComponent } from './payments/gift-cards/gift-cards.component';
import { GiftCardFormComponent } from './payments/gift-card-form/gift-card-form.component';
import { CustomDeliveryModeComponent } from './fulfillment/custom-checkout-delivery-mode/custom-checkout-delivery-mode.component';
import { CustomCheckoutAddressFormComponent } from './components/custom-checkout-address-form/custom-checkout-address-form.component';
import { CustomCheckoutPaymentDetailsComponent } from './payments/custom-checkout-payment-details/custom-checkout-payment-details.component';
import { CustomCheckoutAddressCardsComponent } from './components/custom-checkout-address-cards/custom-checkout-address-cards.component';
import { GiftCardBalanceFormComponent } from './payments/gift-card-balance-form/gift-card-balance-form.component';
import { CustomCheckoutFulfillmentTabsComponent } from './fulfillment/checkout-fullfillment-tabs/checkout-fulfillment-tabs.component';
import { CustomCheckoutOrchestratorComponent } from './components/checkout-orchestrator/checkout-orchestrator.component';
import { CustomCheckoutDeliveryAddressComponent } from './fulfillment/custom-checkout-delivery-address/custom-checkout-delivery-address.component';
import { CheckoutFulfillmentNavTabsComponent } from './fulfillment/checkout-fulfillment-nav-tabs/checkout-fulfillment-nav-tabs.component';
import { CheckoutDeliveryErrorsComponent } from './fulfillment/checkout-delivery-errors/checkout-delivery-errors.component';
import { CustomCheckoutDeliveryModesCardsComponent } from './fulfillment/custom-checkout-delivery-modes-cards/custom-checkout-delivery-modes-cards.component';
import { AdyenIframeModalComponent } from './payments/adyen-iframe-modal/adyen-iframe-modal.component';
import { CheckoutPaymentAddressComponent } from './payments/checkout-payment-address/checkout-payment-address.component';
import { PaymentMethodAccordionComponent } from './payments/payment-method-accordion/payment-method-accordion.component';
import { CustomAddressFinderComponent } from './fulfillment/custom-address-finder/custom-address-finder.component';
import { WhoWillCollectComponent } from './fulfillment/who-will-collect/who-will-collect.component';
import { PaypalComponent } from './payments/paypal/paypal.component';
import { CustomCheckoutCollectionPointComponent } from './fulfillment/custom-checkout-collection-point/custom-checkout-collection-point.component';
import { CustomCheckoutCollectInStoreComponent } from './fulfillment/custom-checkout-collect-in-store/custom-checkout-collect-in-store.component';
import { CustomCisFinderComponent } from './fulfillment/custom-cis-finder/custom-cis-finder.component';
import { AdyenIdealComponent } from './payments/adyen-ideal/adyen-ideal.component';
import { AdyenGiropayComponent } from './payments/adyen-giropay/adyen-giropay.component';
import { CheckoutBasketSummaryComponent } from './components/checkout-basket-summary/checkout-basket-summary.component';

// modules
import { CollectionPointItemModule } from './fulfillment/collection-point-item/collection-point-item.module';

// pipes
import { AddressSelectCardPipe } from 'src/app/pipes/address-select-card/address-select-card.pipe';
import { DeliveryModesCardPipe } from 'src/app/pipes/delivery-modes-card/delivery-modes-card.pipe';
import { DeliveryModesSelectedCardPipe } from 'src/app/pipes/delivery-modes-card/delivery-modes-selected-card.pipe';
import { AddressFormShowFieldPipe } from 'src/app/pipes/address-form-showfield/address-form-show-field.pipe';

// services
import { CustomCartService } from './../../services/cart/facade/cart.service';
import { GeocodeService } from 'src/app/services/google/geocode.service';

// constants
import { environment } from 'src/environments/environment';
import { checkoutTranslations } from 'src/app/translations/app.checkout.translations';
import { PayNowEnabledPipe } from 'src/app/pipes/pay-now-enabled/pay-now-enabled.pipe';
import { LoyaltyVoucherListComponent } from './payments/loyalty-voucher-list/loyalty-voucher-list.component';
import { OrderSummaryComponent } from '../cart-shared/order-summary/order-summary.component';
import { CartSharedModule } from '../cart-shared/cart-shared.module';
import { KlarnaComponent } from './payments/klarna/klarna.component';
import { LoyaltyVoucherComponent } from './payments/loyalty-voucher/loyalty-voucher.component';

@NgModule({
  declarations: [
    // orchestrator
    CustomCheckoutOrchestratorComponent,
    // delivery & fulfillment
    CheckoutFulfillmentNavTabsComponent,
    CustomCheckoutFulfillmentTabsComponent,
    // delivery address
    CustomCheckoutAddressFormComponent,
    CustomCheckoutAddressCardsComponent,
    CustomCheckoutDeliveryAddressComponent,
    // delivery modes
    CustomCheckoutDeliveryModesCardsComponent,
    CustomDeliveryModeComponent,
    CheckoutDeliveryErrorsComponent,
    // collection
    CustomAddressFinderComponent,
    WhoWillCollectComponent,
    CustomCheckoutCollectInStoreComponent,
    CustomCisFinderComponent,
    // payments
    CheckoutPaymentAddressComponent,
    PaymentMethodAccordionComponent,
    CustomCheckoutPaymentDetailsComponent,
    AdyenCreditCardsComponent,
    AdyenSavedCreditCardsComponent,
    GiftCardsComponent,
    GiftCardFormComponent,
    GiftCardBalanceFormComponent,
    PaypalComponent,
    CustomCheckoutCollectionPointComponent,
    AdyenIframeModalComponent,
    AdyenIdealComponent,
    AdyenGiropayComponent,
    // pipes
    AddressSelectCardPipe,
    DeliveryModesCardPipe,
    DeliveryModesSelectedCardPipe,
    AddressFormShowFieldPipe,
    PayNowEnabledPipe,
    KlarnaComponent,
    LoyaltyVoucherComponent,
    LoyaltyVoucherListComponent,
    CheckoutBasketSummaryComponent
  ],
  imports: [
    CommonModule,
    NgbAccordionModule,
    BrowserAnimationsModule,
    NgbAlertModule,
    NgbTabsetModule,
    ReactiveFormsModule,
    CardModule,
    SpinnerModule,
    GenericLinkModule,
    IconModule,
    I18nModule,
    MatSelectModule,
    MatIconModule,
    MatInputModule,
    MatRadioModule,
    MatCheckboxModule,
    NgxPayPalModule,
    CollectionPointItemModule,
    CartSharedModule,
    AgmCoreModule.forRoot({
      apiKey: environment.googleConfig.mapsApiKey
    }),
    ConfigModule.withConfig(checkoutTranslations),
    ConfigModule.withConfig({
      cmsComponents: {
        CheckoutOrchestrator: {
          component: CustomCheckoutOrchestratorComponent,
          guards: [AuthGuard, CartNotEmptyGuard] // TODO Express Checkout Guard
        },
        CheckoutShippingAddress: {
          component: CustomCheckoutFulfillmentTabsComponent,
          deps: [CustomCheckoutPaymentDetailsComponent, CustomDeliveryModeComponent, OrderSummaryComponent]
        },
        CheckoutPaymentDetails: {
          component: CustomCheckoutPaymentDetailsComponent,
          guards: [AuthGuard, CartNotEmptyGuard],
          deps: [CustomCheckoutAddressCardsComponent]
        },
        CheckoutOrderSummary: {
          component: OrderSummaryComponent
        },
        CheckoutDeliveryMode: {
          component: CustomDeliveryModeComponent,
          guards: [AuthGuard, CartNotEmptyGuard],
          deps: [CustomCheckoutDeliveryModesCardsComponent]
        },
        GiftCardBalanceCMSComponent: {
          component: GiftCardBalanceFormComponent
        }
      },
      i18n: { resources: checkoutTranslations }
    })
  ],
  providers: [GeocodeService, CustomCartService],
  entryComponents: [
    CustomCheckoutDeliveryModesCardsComponent,
    CustomCheckoutOrchestratorComponent,
    CustomCheckoutFulfillmentTabsComponent,
    CustomCheckoutPaymentDetailsComponent,
    CustomDeliveryModeComponent,
    CustomCheckoutDeliveryAddressComponent,
    CustomCheckoutAddressCardsComponent,
    GiftCardBalanceFormComponent,
    AdyenIframeModalComponent,
    PaypalComponent
  ]
})
export class CustomCheckoutModule {}
