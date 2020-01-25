import { Component, Input, Output, EventEmitter } from '@angular/core';
import { PaymentMethodsResponse } from 'src/app/interfaces/payment-methods-response.interface';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-adyen-giropay',
  templateUrl: './adyen-giropay.component.html',
  styleUrls: ['./adyen-giropay.component.scss']
})
export class AdyenGiropayComponent {
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
    if (document.querySelector('#giropay') != null) {
      const ideal = this.checkout.create('giropay').mount('#giropay');
    }
  }

  private handleOnChange(event): void {
    this.creditCarDetails.emit(event);
  }
}
