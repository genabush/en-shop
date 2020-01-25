import { Injectable } from '@angular/core';
import { CartVoucherService, StateWithCart, AuthService, Voucher, CartActions } from '@spartacus/core';
import { Store } from '@ngrx/store';
import { OccService } from '../occ/occ.service';
import { CustomCartService } from '../cart/facade/cart.service';
import { HttpClient } from '@angular/common/http';
import { CartPromoVoucherStoreActions, CartPromoVoucherStoreSelectors } from 'src/app/root-store/cart-promo-voucher';

import { isUndefined } from 'lodash';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class CustomCartVoucherService extends CartVoucherService {
  constructor(
    store: Store<StateWithCart>,
    authService: AuthService,
    private occService: OccService,
    private customCartService: CustomCartService,
    private http: HttpClient
  ) {
    super(store, authService);
  }

  getCartVouchersState() {
    return this.store.select(CartPromoVoucherStoreSelectors.getCartPromoVouchersState).pipe(
      map(data => {
        if (!isUndefined(data) && !isUndefined(data.appliedVouchers)) {
          return data.appliedVouchers;
        }
      })
    );
  }

  customApiAddVoucher(voucherId: string) {
    return this.http.post(this.getApiEndpoint('/vouchers?voucherId=' + voucherId), {});
  }

  customApiRemoveVoucher(voucherId: string) {
    return this.http.delete(this.getApiEndpoint('/vouchers/' + voucherId));
  }

  getApiEndpoint(apiMethod: string) {
    const userId = this.customCartService.getUserId();
    return (
      this.occService.getApiUrl() +
      '/users/' +
      (userId ? userId : 'anonymous') +
      '/carts/' +
      this.getActiveCartId() +
      apiMethod
    );
  }

  getActiveCartId() {
    const userId = this.customCartService.getUserId();
    return userId ? this.customCartService.getActiveCartCode() : this.customCartService.getActiveGuid();
  }

  applyVoucherState(appliedVouchers: Voucher[]) {
    if (appliedVouchers.length > 0) {
      // set the applied vouchers store
      this.setCartVoucherStore(appliedVouchers);
    } else {
      this.unSetCartVoucherStore();
    }
  }

  setCartVoucherStore(appliedVouchers: Voucher[]) {
    this.store.dispatch(new CartPromoVoucherStoreActions.SetPromoVoucherAction(appliedVouchers));
  }
  addCartVoucherStore(voucherItem: Voucher) {
    this.store.dispatch(new CartPromoVoucherStoreActions.AddPromoVoucherAction({ voucherItem: voucherItem }));
  }
  unSetCartVoucherStore() {
    this.store.dispatch(new CartPromoVoucherStoreActions.UnSetPromoVoucherAction());
  }
}
