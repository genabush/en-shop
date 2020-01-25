/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
import { Component, OnInit, Input } from '@angular/core';

// interfaces
import { IDeliveryRestrictedState, IDeliveryModesRestrictedState } from 'src/app/interfaces/custom-checkout.interface';

@Component({
  selector: 'cx-checkout-delivery-errors',
  templateUrl: './checkout-delivery-errors.component.html'
})
export class CheckoutDeliveryErrorsComponent {
  @Input() deliveryRestricted: IDeliveryRestrictedState;
  @Input() deliveryModesRestricted: IDeliveryModesRestrictedState;
}
