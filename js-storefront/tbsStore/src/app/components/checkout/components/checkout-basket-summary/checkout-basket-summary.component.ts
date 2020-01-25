import { Component, OnInit } from '@angular/core';
import { Observable, BehaviorSubject, Subscription } from 'rxjs';
import { CustomCart } from 'src/app/interfaces/custom-cart.interface';
import { OrderEntry, Cart } from '@spartacus/core';
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';
import { ICON_TYPE } from '@spartacus/storefront';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'cx-checkout-basket-summary',
  templateUrl: './checkout-basket-summary.component.html'
})
export class CheckoutBasketSummaryComponent implements OnInit {
  cart$: Observable<Cart | CustomCart>;
  entries$: BehaviorSubject<OrderEntry[]> = new BehaviorSubject<OrderEntry[]>([]);
  cartLoaded$: Observable<boolean>;
  cartTotalItems = 0;
  iconTypes = ICON_TYPE;
  subscription = new Subscription();
  constructor(protected cartService: CustomCartService) {}

  ngOnInit() {
    this.cart$ = this.cartService.getActive();
    this.setEntriesSub();
    this.cartLoaded$ = this.cartService.getLoaded();
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
}
