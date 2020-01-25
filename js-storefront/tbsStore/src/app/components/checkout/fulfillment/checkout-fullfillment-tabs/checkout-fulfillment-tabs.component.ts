import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';

// interfaces
import { IDeliveryRestrictedState, CheckoutFulfillmentTabs } from 'src/app/interfaces/custom-checkout.interface';

@Component({
  selector: 'cx-checkout-fullfillment-tabs',
  templateUrl: './checkout-fulfillment-tabs.component.html'
})
export class CustomCheckoutFulfillmentTabsComponent implements OnInit {
  @Input() checkoutJourneyType: number;
  @Output() fulfillmentStateEmit: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Output() deliveryRestrictedEmit: EventEmitter<IDeliveryRestrictedState> = new EventEmitter<
    IDeliveryRestrictedState
  >();
  @Input() deliveryRestricted: IDeliveryRestrictedState;
  checkoutFulfillmentTabs = CheckoutFulfillmentTabs;
  constructor() {}

  ngOnInit() {}
  setCheckoutJourneyType(type: number) {
    this.checkoutJourneyType = type;
  }

  stateEmit(currentState: boolean) {
    this.fulfillmentStateEmit.emit(currentState);
  }

  setDeliveryRestricted(deliveryRestrictedState: IDeliveryRestrictedState) {
    this.deliveryRestrictedEmit.emit(deliveryRestrictedState);
  }
}
