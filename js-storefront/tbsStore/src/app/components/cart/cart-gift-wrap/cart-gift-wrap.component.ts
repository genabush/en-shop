/*
 * Copyright (c)
 * 2020 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
import { Component, HostBinding } from '@angular/core';

// vendor
import { tap } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { isNull } from 'lodash';

// spartacus
import { ICON_TYPE, CmsComponentData } from '@spartacus/storefront';
import { Cart, CmsBannerComponent, GlobalMessageService, GlobalMessageType, Translatable } from '@spartacus/core';

// services
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';

// intefraces
import { CustomCart } from 'src/app/interfaces/custom-cart.interface';
import { AppCustomStorageService } from 'src/app/services/product/web-storage.service';

// constants
const STORAGE_KEY = 'giftWrapApplied';

@Component({
  selector: 'cx-cart-gift-wrap',
  templateUrl: './cart-gift-wrap.component.html'
})
export class CartGiftWrapComponent {
  @HostBinding('class') classes = 'cart-gift-wrap__wrapper mb-5';

  activeCart$: Observable<CustomCart | Cart>;
  iconTypes = ICON_TYPE;

  cmsData$: Observable<CmsBannerComponent> = this.componentData.data$;

  constructor(
    protected componentData: CmsComponentData<CmsBannerComponent>,
    private cartService: CustomCartService,
    private sessionStorage: AppCustomStorageService,
    private globalMessageService: GlobalMessageService
  ) {
    this.activeCart$ = this.cartService.getActive().pipe(
      tap((cart: CustomCart) => {
        this.checkSessionGiftWrapApplied(cart);
      })
    );
  }

  getSessionGiftWrapApplied() {
    return this.sessionStorage.getSessionStorageItem(STORAGE_KEY);
  }
  checkSessionGiftWrapApplied(cart: CustomCart) {
    const sessionApplied = isNull(this.getSessionGiftWrapApplied()) ? false : true;
    if (!cart.eligibleForGiftWrap && sessionApplied) {
      // global messaging should be triggered to notify the user
      const translateMsg = {
        key: 'cart.globalMessages.giftWrapRemoved'
      } as Translatable;
      this.globalMessageService.add(translateMsg, GlobalMessageType.MSG_TYPE_INFO);
    }
  }
  triggerCartReload() {
    this.cartService.reloadActiveCart();
  }

  setSessionGiftWrapApplied() {
    this.sessionStorage.setSessionStorageItem(STORAGE_KEY, true);
    this.triggerCartReload();
  }
  unSetSessionGiftWrapApplied() {
    this.sessionStorage.removeSessionStorageItem(STORAGE_KEY);
    this.triggerCartReload();
  }
}
