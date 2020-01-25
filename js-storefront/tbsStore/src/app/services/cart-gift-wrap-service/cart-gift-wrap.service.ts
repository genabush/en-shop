import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

// services
import { OccService } from '../occ/occ.service';
import { CustomCartService } from '../cart/facade/cart.service';

// interfaces
import { IGiftWrapMessage, CustomCart } from 'src/app/interfaces/custom-cart.interface';
import { Store } from '@ngrx/store';
import { CartGiftWrapStoreActions, CartGiftWrapStoreSelectors } from 'src/app/root-store/cart-gift-wrap';
import { map } from 'rxjs/operators';

import { isUndefined } from 'lodash';

@Injectable({
  providedIn: 'root'
})
export class CartGiftWrapService {
  activeCart: CustomCart;
  constructor(
    protected http: HttpClient,
    protected occService: OccService,
    protected customCartService: CustomCartService,
    private store: Store<any>
  ) {}

  getGiftWrapFromState() {
    return this.store.select(CartGiftWrapStoreSelectors.getCartGiftWrapState).pipe(
      map(data => {
        if (!isUndefined(data) && !isUndefined(data.giftMessage)) {
          return {
            giftMessage: data.giftMessage,
            giftMessageSenderName: data.giftMessageSenderName,
            giftMessageName: data.giftMessageName
          };
        }
      })
    );
  }

  getGiftWrapServiceFromState() {
    return this.store.select(CartGiftWrapStoreSelectors.getCartGiftWrapState).pipe(
      map(data => {
        if (!isUndefined(data) && !isUndefined(data.giftWrapApplied)) {
          return {
            giftWrapApplied: data.giftWrapApplied,
            giftWrapServiceImage: data.giftWrapServiceImage,
            giftWrapServiceMessage: data.giftWrapServiceMessage
          };
        }
      })
    );
  }

  addGiftWrapMessage(giftMessage: IGiftWrapMessage) {
    return this.http.post(this.getApiEndpoint('/giftwrap/message'), giftMessage);
  }

  removeGiftWrapMessage() {
    return this.http.post(this.getApiEndpoint('/giftwrap/message'), {});
  }

  addGiftWrapService() {
    return this.http.post(this.getApiEndpoint('/giftwrap/add'), {});
  }

  removeGiftWrapService() {
    return this.http.post(this.getApiEndpoint('/giftwrap/remove'), {});
  }

  getApiEndpoint(apiMethod: string) {
    const userId = this.customCartService.getUserId();
    return (
      this.occService.getApiUrl() +
      '/users/' +
      (userId ? userId : 'anonymous') +
      '/carts/' +
      (userId ? this.customCartService.getActiveCartCode() : this.customCartService.getActiveGuid()) +
      apiMethod
    );
  }

  setStoreGiftWrapMessage(giftMessage: IGiftWrapMessage) {
    this.store.dispatch(new CartGiftWrapStoreActions.SetGiftWrapMessageAction({ giftMessage: giftMessage }));
  }
  unsetStoreGiftWrapMessage() {
    this.store.dispatch(new CartGiftWrapStoreActions.UnSetGiftWrapMessageAction({}));
  }
  setStoreGiftWrapService(giftWrapService: IGiftWrapMessage) {
    this.store.dispatch(new CartGiftWrapStoreActions.SetGiftWrapServiceAction({ giftWrap: giftWrapService }));
  }
  unsetStoreGiftWrapService(giftWrapService: IGiftWrapMessage) {
    this.store.dispatch(new CartGiftWrapStoreActions.UnSetGiftWrapServiceAction({ giftWrap: giftWrapService }));
  }
}
