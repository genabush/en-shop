import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';

// spartacus
import { Cart, OrderEntry } from '@spartacus/core';

// vendor
import { Observable, BehaviorSubject, Subscription } from 'rxjs';
import { filter } from 'rxjs/operators';

// services
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';
import { WishlistService } from '../../../services/wishlist/wishlist.service';

@Component({
  selector: 'cx-cart-details',
  templateUrl: './cart-details.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CartDetailsComponent implements OnInit {
  cart$: Observable<Cart>;
  entries$: BehaviorSubject<OrderEntry[]> = new BehaviorSubject<OrderEntry[]>([]);
  cartLoaded$: Observable<boolean>;
  cartTotalItems = 0;
  subscription = new Subscription();
  isMovedToWishlist = false;

  constructor(protected cartService: CustomCartService, protected wishlistService: WishlistService) {}

  ngOnInit() {
    this.cart$ = this.cartService.getActive();
    this.setEntriesSub();
    this.cartLoaded$ = this.cartService.getLoaded();
    this.wishlistService.resetAddFromCart();
  }

  setEntriesSub() {
    this.subscription.add(
      this.cartService
        .getEntries()
        .pipe(filter(entries => entries.length > 0))
        .subscribe((entryArray: OrderEntry[]) => {
          this.cartTotalItems = 0;
          if (entryArray.length > 0) {
            entryArray.map((entryItem: OrderEntry) => (this.cartTotalItems += entryItem.quantity));
            this.entries$.next(entryArray);
          }
        })
    );
  }

  getAllPromotionsForCart(cart: Cart): any[] {
    return this.cartService.getAllPromotions(cart);
  }

  shoppingBagToWishlist(): void {
    // TODO: Check if user logged in and perform different logic.
    const itemsToPushToSession = [];
    this.cartService.getEntries().subscribe((cartEntries: OrderEntry[]) => {
      cartEntries.forEach((entry: OrderEntry) => {
        itemsToPushToSession.push(entry.product.code);
      });
    });

    if (itemsToPushToSession.length > 0) {
      itemsToPushToSession.forEach((productCode: string) => {
        let existing = this.wishlistService.getItemExistingProductInGuestWishlist() || [];
        this.wishlistService.guestSetToSessionWishlist(
          this.wishlistService.createsOrAddsProductToList(existing, productCode)
        );
      });

      this.wishlistService.openAddFromCartModal();
      this.wishlistService.addFromCart();
      // Display Modal Popup here!
      this.isMovedToWishlist = true;
    }
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
