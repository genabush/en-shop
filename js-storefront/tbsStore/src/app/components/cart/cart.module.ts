import { NgModule } from '@angular/core';

import { CartModule, ConfigModule, I18nModule } from '@spartacus/core';
import { PAGE_LAYOUT_HANDLER } from '@spartacus/storefront';
import { IconModule } from '@spartacus/storefront';

// vendor
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

// modules
import { AddToCartModule } from './add-to-cart/add-to-cart.module';
import { CartDetailsModule } from './cart-details/cart-details.module';
import { CartSharedModule } from '../cart-shared/cart-shared.module';
import { CartTotalsModule } from './cart-totals/cart-totals.module';
import { CustomMiniCartModule } from '../mini-cart/custom-mini-cart.module';
import { CartEmptyParagraphModule } from './cart-empty-paragraph/cart-empty-paragraph.module';
import { CartGiftWrapModule } from './cart-gift-wrap/cart-gift-wrap.module';

// constants
import { CartPageLayoutHandler } from './cart-page-layout-handler';
import { cartTranslations } from 'src/app/translations/app.cart.translations';
import { CartApplyCouponsModule } from './cart-apply-coupon/cart-apply-coupons.module';

@NgModule({
  imports: [
    NgbModule,
    CartDetailsModule,
    CartTotalsModule,
    CartSharedModule,
    CustomMiniCartModule,
    CartEmptyParagraphModule,
    CartApplyCouponsModule,
    CartGiftWrapModule,
    IconModule,
    I18nModule,
    ConfigModule.withConfig(cartTranslations),
    ConfigModule.withConfig({
      i18n: { resources: cartTranslations }
    })
  ],
  exports: [
    CartDetailsModule,
    CartTotalsModule,
    CartSharedModule,
    AddToCartModule,
    CartModule,
    CustomMiniCartModule,
    CartEmptyParagraphModule
  ],
  providers: [
    {
      provide: PAGE_LAYOUT_HANDLER,
      useClass: CartPageLayoutHandler,
      multi: true
    }
  ]
})
export class CartComponentModule {}
