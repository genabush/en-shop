import { ChangeDetectionStrategy, Component, OnInit, HostBinding } from '@angular/core';
import { Observable } from 'rxjs';
import { filter } from 'rxjs/operators';

import { Cart, OrderEntry, CartService } from '@spartacus/core';

@Component({
  selector: 'cx-cart-totals',
  templateUrl: './cart-totals.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CartTotalsComponent implements OnInit {
  @HostBinding('class') classs = 'px-3 px-lg-0';
  cart$: Observable<Cart>;
  entries$: Observable<OrderEntry[]>;

  constructor(protected cartService: CartService) {}

  ngOnInit() {
    this.cart$ = this.cartService.getActive();
    this.entries$ = this.cartService.getEntries().pipe(filter(entries => entries.length > 0));
  }
}
