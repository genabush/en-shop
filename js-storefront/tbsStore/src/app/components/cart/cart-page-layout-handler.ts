import { Injectable, OnDestroy } from '@angular/core';

// spartacus
import { PageLayoutHandler } from '@spartacus/storefront';
import { RoutingService, RouterState } from '@spartacus/core';

// vendor
import { combineLatest, Observable, Subscription } from 'rxjs';
import { map } from 'rxjs/operators';
import { isUndefined } from 'lodash';

// services
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';

// interfaces
import { CustomCart } from 'src/app/interfaces/custom-cart.interface';

@Injectable()
export class CartPageLayoutHandler implements PageLayoutHandler, OnDestroy {
  routingState: RouterState;
  subscription = new Subscription();
  constructor(private cartService: CustomCartService, private routingService: RoutingService) {
    this.subscription.add(
      this.routingService.getRouterState().subscribe((state: RouterState) => {
        if (!isUndefined(state)) {
          if (this.isCartPage(state)) {
            this.routingState = state;
          }
        }
      })
    );
  }

  handle(slots$: Observable<string[]>, pageTemplate?: string, section?: string) {
    if (pageTemplate === 'CartPageTemplate' && !section) {
      // TODO: Combine latest is deprecated. Please replace.
      // TODO: Investigate rerouting to homepage when cart empty.
      return combineLatest(slots$, this.cartService.getActive()).pipe(
        map(([slots, cart]) => {
          const customCart: CustomCart = cart as CustomCart;
          if (this.isNotEmptyCart(customCart)) {
            if (customCart.eligibleForGiftWrap && !customCart.eligibleForGiftMessage) {
              return slots.filter(slot => slot !== 'EmptyCartMiddleContent' && slot !== 'Section1');
            } else {
              return slots.filter(slot => slot !== 'EmptyCartMiddleContent');
            }
          } else {
            if (!isUndefined(this.routingState)) {
              if (this.isNotInitialNav(this.routingState) && this.isCartPage(this.routingState)) {
                return slots.filter(slot => slot !== 'CenterContentSlot');
              }
            }
            this.routingState = undefined;
            this.routingService.goByUrl('/');
            return [];
          }
        })
      );
    }
    return slots$;
  }
  private isNotEmptyCart(cart: CustomCart) {
    return !isUndefined(cart.totalItems) && cart.totalItems !== 0;
  }
  private isNotInitialNav(state: RouterState): boolean {
    return state.navigationId !== 1;
  }

  private isCartPage(state: RouterState): boolean {
    return state.state.url.indexOf('cart') > -1;
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
