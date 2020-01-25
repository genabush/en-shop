import { Component, Input, Output, EventEmitter } from '@angular/core';
import { PaymentMethodsResponse } from 'src/app/interfaces/payment-methods-response.interface';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-adyen-ideal',
  templateUrl: './adyen-ideal.component.html',
  styleUrls: ['./adyen-ideal.component.scss']
})
export class AdyenIdealComponent {
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

  private checkout;
  private adyenConfiguration = {
    locale: environment.adyenConfig.locale,
    environment: environment.adyenConfig.environment,
    originKey: '',
    paymentMethodsResponse: null,
    onChange: this.handleOnChange.bind(this)
  };

  private initializeAdyen(): void {
    this.checkout = new window['AdyenCheckout'](this.adyenConfiguration);
    if (document.querySelector('#ideal-container') != null) {
      const ideal = this.checkout
        .create('ideal', {
          showImage: true, // Optional. Set to **false** to remove the bank logos from the iDEAL form.

          issuer: '0031' // Optional. Set this to an **id** of an iDEAL issuer to preselect it.
        })
        .mount('#ideal-container');
    }
  }

  private handleOnChange(event): void {
    this.creditCarDetails.emit(event);
  }
}
