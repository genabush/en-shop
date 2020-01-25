import { Component, Input, EventEmitter, Output } from '@angular/core';
import { WishlistService } from 'src/app/services/wishlist/wishlist.service';
import { ICON_TYPE } from '@spartacus/storefront';

@Component({
  selector: 'app-wishlist-product-item',
  templateUrl: './wishlist-product-item.component.html'
})
export class WishlistProductItemComponent {
  @Input() product;
  @Output() removeProductEmit = new EventEmitter<string>();
  iconTypes = ICON_TYPE;

  constructor(public wishlistService: WishlistService) {}

  triggerRemoveProduct(code) {
    this.removeProductEmit.emit(code);
  }
}
