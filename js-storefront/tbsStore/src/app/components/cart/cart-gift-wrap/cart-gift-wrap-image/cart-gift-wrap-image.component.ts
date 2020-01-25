/*
 * Copyright (c)
 * 2020 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
import { Component, Input } from '@angular/core';

@Component({
  selector: 'cx-cart-gift-wrap-image',
  templateUrl: './cart-gift-wrap-image.component.html'
})
export class CartGiftWrapImageComponent {
  @Input() giftWrapImageUrl: string;
  @Input() altText: string;
  constructor() {}
}
