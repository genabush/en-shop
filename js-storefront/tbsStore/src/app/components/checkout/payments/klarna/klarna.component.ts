import { Component, Input, Output, EventEmitter } from '@angular/core';
import { PaymentMethodsResponse } from 'src/app/interfaces/payment-methods-response.interface';
import { environment } from 'src/environments/environment';

type KlartnaTypes = 'klarna_paynow' | 'klarna' | 'klarna_account';

@Component({
  selector: 'app-klarna',
  templateUrl: './klarna.component.html',
  styleUrls: ['./klarna.component.scss']
})
export class KlarnaComponent {
  @Input() type: KlartnaTypes = 'klarna';
  @Input() set paymentMethod(val: PaymentMethodsResponse) {
    if (!!val) {
      this.adyenConfiguration.paymentMethodsResponse = val;
      this.adyenConfiguration.originKey = val.adyenOriginKey;
      if (!!val.paymentMethods && val.paymentMethods.length > 0) {
        setTimeout(() => this.initializeAdyen());
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
    if (document.querySelector('#klarna-container') != null) {
      const klarna = this.checkout.create(this.type).mount('#klarna-container');
      if (this.type === 'klarna_account' || this.type === 'klarna_paynow') {
        this.handleOnChange({ isValid: true, data: { paymentMethod: { type: this.type } } });
      }
    }
  }

  private handleOnChange(event): void {
    this.creditCarDetails.emit(event);
  }
}
