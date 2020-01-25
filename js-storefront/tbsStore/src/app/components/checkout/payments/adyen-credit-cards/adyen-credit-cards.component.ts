import { Component, Output, EventEmitter, Input, OnInit } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatCheckboxChange } from '@angular/material';

import { environment } from '../../../../../environments/environment';
// interfaces
import { PaymentMethodsResponse } from '../../../../interfaces/payment-methods-response.interface';
import { AdyenCreditCards } from './../../../../interfaces/adyen-credit-cards.interface';
// services
import { AdyenCreditCardsService } from './../../../../services/adyen-credit-cards/adyen-credit-cards.service';
@Component({
  selector: 'app-adyen-credit-cards',
  templateUrl: './adyen-credit-cards.component.html',
  styleUrls: ['./adyen-credit-cards.component.scss']
})
export class AdyenCreditCardsComponent implements OnInit {
  saveCardPaymentForm: FormGroup = this.getSaveCardPaymentGroup();
  isCardPaymentSaved = false;
  images: AdyenCreditCards;
  @Input() set paymentMethod(val: PaymentMethodsResponse) {
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
  @Output() creditCarDetails = new EventEmitter();
  @Output() hasSavedCardChecked = new EventEmitter();

  private checkout;
  private adyenConfiguration = {
    locale: environment.adyenConfig.locale,
    environment: environment.adyenConfig.environment,
    originKey: '',
    paymentMethodsResponse: null,
    onChange: this.handleOnChange.bind(this)
  };

  constructor(private fb: FormBuilder, private addImg: AdyenCreditCardsService) {}

  private initializeAdyen(): void {
    this.checkout = new window['AdyenCheckout'](this.adyenConfiguration);
    if (document.querySelector('#component-container') != null) {
      this.checkout
        .create('card', {
          hasHolderName: true,
          holderNameRequired: true,
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
        .mount('#component-container');
    }
  }

  private handleOnChange(event): void {
    this.creditCarDetails.emit(event);
  }

  getSaveCardPaymentGroup() {
    return this.fb.group({
      saveCardPayment: ['']
    });
  }

  savedCardSelected(event: MatCheckboxChange) {
    this.saveCardPaymentForm.patchValue({
      saveCardPayment: event.checked
    });
    this.isCardPaymentSaved = this.saveCardPaymentForm.get(['saveCardPayment']).value;
    this.hasSavedCardChecked.emit(this.isCardPaymentSaved);
  }
  ngOnInit(): void {
    this.images = this.addImg.getImages();
  }
}
