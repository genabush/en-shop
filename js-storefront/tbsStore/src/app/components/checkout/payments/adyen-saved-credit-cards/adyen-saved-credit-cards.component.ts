/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
import { Component, Output, EventEmitter, Input } from '@angular/core';
import { environment } from '../../../../../environments/environment';

// interfaces
import { PaymentMethodsResponse } from '../../../../interfaces/payment-methods-response.interface';
@Component({
  selector: 'app-adyen-saved-credit-cards',
  templateUrl: './adyen-saved-credit-cards.component.html',
  styleUrls: ['./adyen-saved-credit-cards.component.scss']
})
export class AdyenSavedCreditCardsComponent {
  @Input() set savedPaymentMethod(val: PaymentMethodsResponse) {
    if (!!val) {
      this.adyenConfiguration.paymentMethodsResponse = val;
      this.adyenConfiguration.originKey = val.adyenOriginKey;
      if (!!val.paymentMethods && val.paymentMethods.length > 0) {
        if (val.creditCardPayment) {
          this.initializeAdyen();
        }
      }
    }
  }
  @Output() savedCreditCarDetails = new EventEmitter();

  private checkout;
  private adyenConfiguration = {
    locale: environment.adyenConfig.locale,
    environment: environment.adyenConfig.environment,
    originKey: '',
    paymentMethodsResponse: null,
    onChange: this.handleOnChange.bind(this)
  };

  constructor() {}

  private initializeAdyen(): void {
    this.checkout = new window['AdyenCheckout'](this.adyenConfiguration);
    if (document.querySelector('#customCard-container') != null) {
      this.checkout
        .create('securedfields', {
          allowedDOMAccess: true,
          autoFocus: true,
          paymentMethodsConfiguration: {
            card: {
              showStoredPaymentMethods: true
            }
          },
          styles: {
            error: {
              color: 'red'
            },
            validated: {
              color: 'green'
            },
            placeholder: {
              color: '#d8d8d8'
            }
          }
        })
        .mount('#customCard-container');
    }
  }

  private handleOnChange(event): void {
    this.savedCreditCarDetails.emit(event);
  }
}
