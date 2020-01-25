import { Component, OnInit } from '@angular/core';
import { ParagraphComponent, CmsComponentData } from '@spartacus/storefront';
import { CmsParagraphComponent } from '@spartacus/core';
import { WishlistService } from '../../../services/wishlist/wishlist.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'cx-cart-empty-paragraph',
  templateUrl: './cart-empty-paragraph.component.html'
})
export class CartEmptyParagraphComponent extends ParagraphComponent implements OnInit {
  isMovedToWishlist$: Observable<any>;
  constructor(public component: CmsComponentData<CmsParagraphComponent>, private wishlistServices: WishlistService) {
    super(component);
  }

  ngOnInit() {
    this.isMovedToWishlist$ = this.wishlistServices.getUserWishlists();
  }
}
