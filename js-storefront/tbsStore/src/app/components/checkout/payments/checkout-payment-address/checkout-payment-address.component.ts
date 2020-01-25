/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

// vendor
import { BehaviorSubject, Observable } from 'rxjs';

// interfaces
import { ICustomAddress, CheckoutFulfillmentTabs } from 'src/app/interfaces/custom-checkout.interface';

@Component({
  selector: 'cx-checkout-payment-address',
  templateUrl: './checkout-payment-address.component.html'
})
export class CheckoutPaymentAddressComponent implements OnInit {
  @Input() isLoadingAddresses: boolean;
  @Input() checkoutJourneyType: number;
  @Input() sameAsShippingAddress: boolean;
  @Input() selectedBillingAddress: string | null;
  @Input() shippingAddress$: Observable<ICustomAddress>;
  @Input() existingAddresses$: BehaviorSubject<ICustomAddress[]> = new BehaviorSubject<ICustomAddress[]>([]);
  @Output() addressEditEvent: EventEmitter<any> = new EventEmitter<any>();
  @Output() addressAddEvent: EventEmitter<any> = new EventEmitter<any>();
  @Output() addressSelectedEvent: EventEmitter<ICustomAddress> = new EventEmitter<ICustomAddress>();
  @Output() toggleShippingAddressEvent: EventEmitter<any> = new EventEmitter<any>();
  checkoutFulfillmentTabs = CheckoutFulfillmentTabs;
  constructor() {}

  ngOnInit() {}
}
