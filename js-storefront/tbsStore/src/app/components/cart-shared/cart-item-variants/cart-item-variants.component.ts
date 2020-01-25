import { Component, Input } from '@angular/core';

@Component({
  selector: 'cx-cart-item-variants',
  templateUrl: './cart-item-variants.component.html'
})
export class CartItemVariantsComponent {
  @Input() product: any;
}
