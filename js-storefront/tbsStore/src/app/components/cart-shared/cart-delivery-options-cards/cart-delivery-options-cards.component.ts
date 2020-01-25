import { Component, OnInit, Input } from '@angular/core';

import { DeliveryMode } from '@spartacus/core';

@Component({
  selector: 'cx-cart-delivery-options-cards',
  templateUrl: './cart-delivery-options-cards.component.html'
})
export class CartDeliveryOptionsCardsComponent implements OnInit {
  @Input() deliveryModes: DeliveryMode[];
  constructor() {}

  ngOnInit() {}
}
