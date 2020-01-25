import { Injectable } from '@angular/core';

// vendor
import { select, Store } from '@ngrx/store';
import { asyncScheduler, combineLatest, Observable, Subscription } from 'rxjs';
import { debounceTime, filter, map, shareReplay, take, tap } from 'rxjs/operators';

// spartacus
import { AuthService } from '@spartacus/core';
import { Cart } from '@spartacus/core';
import { OrderEntry } from '@spartacus/core';
import { CartActions } from '@spartacus/core';
import { StateWithCart } from '@spartacus/core';
import { CartSelectors } from '@spartacus/core';
import { ANONYMOUS_USERID, CartDataService } from '@spartacus/core';

// constants
import { ProductVariantSelectionStoreSelectors } from '../../../root-store/product-variant-selection';
import { RootStoreState } from '../../../root-store';
import { isUndefined } from 'lodash';

@Injectable()
export class CustomCartService {
  private readonly PREVIOUS_USER_ID_INITIAL_VALUE = 'PREVIOUS_USER_ID_INITIAL_VALUE';
  private previousUserId = this.PREVIOUS_USER_ID_INITIAL_VALUE;
  private _activeCart$: Observable<Cart>;
  private carCode: string;
  private cartGuid: string;
  private userId: string;
  cartMergeRequired = false;
  cartMergeCompleteSub: Subscription;
  constructor(
    protected store: Store<StateWithCart>,
    public storeRoot: Store<RootStoreState.State>,
    protected cartData: CartDataService,
    protected authService: AuthService
  ) {
    this._activeCart$ = combineLatest([
      this.store.select(CartSelectors.getCartContent),
      this.store.select(CartSelectors.getCartLoading),
      this.authService.getUserToken()
    ]).pipe(
      // combineLatest emits multiple times on each property update instead of one emit
      // additionally dispatching actions that changes selectors used here needs to happen in order
      // for this asyncScheduler is used here
      debounceTime(1, asyncScheduler),
      filter(([, loading]) => !loading),
      tap(([cart, , userToken]) => {
        if (this.isJustLoggedIn(userToken.userId)) {
          this.loadOrMerge();
        } else if (this.isCreated(cart) && this.isIncomplete(cart)) {
          this.load();
        }
        this.carCode = cart.code;
        this.cartGuid = cart.guid;
        this.userId = userToken.userId;
        this.previousUserId = userToken.userId;
      }),
      filter(([cart]) => !this.isCreated(cart) || (this.isCreated(cart) && !this.isIncomplete(cart))),
      map(([cart]) => cart),
      shareReplay({ bufferSize: 1, refCount: true })
    );
  }

  getActive(): Observable<Cart> {
    return this._activeCart$;
  }

  getEntries(): Observable<OrderEntry[]> {
    return this.store.pipe(select(CartSelectors.getCartEntries));
  }

  getCartMergeComplete(): Observable<boolean> {
    return this.store.pipe(select(CartSelectors.getCartMergeComplete));
  }

  getLoaded(): Observable<boolean> {
    return this.store.pipe(select(CartSelectors.getCartLoaded));
  }

  getActiveCartCode(): string {
    return this.carCode;
  }

  getUserId(): string {
    return this.userId;
  }

  clearCart(): void {
    return this.store.dispatch(new CartActions.ClearCart());
  }

  getActiveGuid() {
    return this.cartGuid;
  }

  reloadActiveCart() {
    this.load();
  }

  private loadOrMerge(): void {
    // for login user, whenever there's an existing cart, we will load the user
    // current cart and merge it into the existing cart
    if (!this.isCreated(this.cartData.cart)) {
      this.cartMergeRequired = false;
      this.store.dispatch(
        new CartActions.LoadCart({
          userId: this.cartData.userId,
          cartId: 'current'
        })
      );
    } else {
      this.cartMergeRequired = true;
      this.setCartMergeSub();
      this.store.dispatch(
        new CartActions.MergeCart({
          userId: this.cartData.userId,
          cartId: this.cartData.cart.guid
        })
      );
    }
  }

  private setCartMergeSub() {
    this.cartMergeCompleteSub = this.getCartMergeComplete().subscribe(() => {
      this.cartMergeRequired = false;
      this.destroyCartMergeSub();
    });
  }

  private destroyCartMergeSub() {
    if (this.cartMergeCompleteSub) {
      this.cartMergeCompleteSub.unsubscribe();
    }
  }

  private load(): void {
    this.cartMergeRequired = false;
    if (this.cartData.userId !== ANONYMOUS_USERID) {
      this.store.dispatch(
        new CartActions.LoadCart({
          userId: this.cartData.userId,
          cartId: this.cartData.cartId ? this.cartData.cartId : 'current'
        })
      );
    } else {
      this.store.dispatch(
        new CartActions.LoadCart({
          userId: this.cartData.userId,
          cartId: this.cartData.cartId
        })
      );
    }
  }

  addEntry(productCode: string, quantity: number, entry?: OrderEntry): void {
    this.storeRoot.select(ProductVariantSelectionStoreSelectors.getSelectedProductState).subscribe(data => {
      if (data.selected.code && data.selected.code !== null) {
        // No other variants were selected
        productCode = data.selected.code;
      }
    });
    this.store
      .pipe(
        select(CartSelectors.getActiveCartState),
        tap(cartState => {
          if (!this.isCreated(cartState.value.content) && !cartState.loading) {
            this.store.dispatch(new CartActions.CreateCart({ userId: this.cartData.userId }));
          }
        }),
        filter(cartState => this.isCreated(cartState.value.content)),
        take(1)
      )
      .subscribe(_ => {
        if (isUndefined(entry)) {
          // NEW entry
          this.store.dispatch(
            new CartActions.CartAddEntry({
              userId: this.cartData.userId,
              cartId: this.cartData.cartId,
              productCode: productCode,
              quantity: quantity
            })
          );
        } else {
          // EXISTING entry
          this.store.dispatch(
            new CartActions.CartUpdateEntry({
              userId: this.cartData.userId,
              cartId: this.cartData.cartId,
              entry: entry.entryNumber,
              productCode: productCode,
              qty: quantity
            })
          );
        }
      });
  }

  removeEntry(entry: OrderEntry): void {
    this.store.dispatch(
      new CartActions.CartRemoveEntry({
        userId: this.cartData.userId,
        cartId: this.cartData.cartId,
        entry: entry.entryNumber
      })
    );
  }

  updateEntry(entryNumber: string, quantity: number): void {
    if (quantity > 0) {
      this.store.dispatch(
        new CartActions.CartUpdateEntry({
          userId: this.cartData.userId,
          cartId: this.cartData.cartId,
          entry: entryNumber,
          qty: quantity
        })
      );
    } else {
      this.store.dispatch(
        new CartActions.CartRemoveEntry({
          userId: this.cartData.userId,
          cartId: this.cartData.cartId,
          entry: entryNumber
        })
      );
    }
  }

  getEntry(productCode: string): Observable<OrderEntry> {
    return this.store.pipe(select(CartSelectors.getCartEntrySelectorFactory(productCode)));
  }

  getAllPromotions(cart: Cart): any[] {
    const potentialPromotions = [];
    potentialPromotions.push(...(cart.potentialOrderPromotions || []));
    potentialPromotions.push(...(cart.potentialProductPromotions || []));

    const appliedPromotions = [];
    appliedPromotions.push(...(cart.appliedOrderPromotions || []));
    appliedPromotions.push(...(cart.appliedProductPromotions || []));

    return [...potentialPromotions, ...appliedPromotions];
  }

  private isCreated(cart: Cart): boolean {
    return cart && typeof cart.guid !== 'undefined';
  }

  /**
   * Cart is incomplete if it contains only `guid` and `code` properties, which come from local storage.
   * To get cart content, we need to load cart from backend.
   * Update: There are cases where 3 keys are attached to the cart (loggedin user)
   */
  private isIncomplete(cart: Cart): boolean {
    return !isUndefined(cart) && Object.keys(cart).length <= 3;
  }

  private isJustLoggedIn(userId: string): boolean {
    return (
      typeof userId !== 'undefined' && // logged in user
      this.previousUserId !== userId && // *just* logged in
      this.previousUserId !== this.PREVIOUS_USER_ID_INITIAL_VALUE // not app initialization
    );
  }
}
