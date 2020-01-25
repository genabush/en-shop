/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
import { Component, EventEmitter, Output, Input } from '@angular/core';

// interfaces
import { CheckoutFulfillmentTabs } from 'src/app/interfaces/custom-checkout.interface';
import { IFullfillmentEligibilityState } from 'src/app/interfaces/custom-cart.interface';

@Component({
  selector: 'cx-checkout-fulfillment-nav-tabs',
  templateUrl: './checkout-fulfillment-nav-tabs.component.html'
})
export class CheckoutFulfillmentNavTabsComponent {
  @Input() checkoutJourneyType: number;
  @Input() fulfillmentEligibilityState: IFullfillmentEligibilityState;
  @Output() setJourneyType: EventEmitter<number> = new EventEmitter<number>();
  checkoutFulfillmentTabs = CheckoutFulfillmentTabs;
  constructor() {}

  setCheckoutJourneyType(journeyType: number) {
    this.setJourneyType.emit(journeyType);
  }
}
