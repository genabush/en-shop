import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-wishlist-products-listing-refine',
  templateUrl: './wishlist-products-listing-refine.component.html'
})
export class WishlistProductsListingRefineComponent {
  @Input() count: number = 0;
}
