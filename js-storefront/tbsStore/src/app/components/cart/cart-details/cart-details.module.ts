import { CommonModule } from '@angular/common';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

// spartacus
import { CmsConfig, ConfigModule, I18nModule, UrlModule } from '@spartacus/core';
import { IconModule, MediaModule } from '@spartacus/storefront';

// modules
import { CartSharedModule } from '../../cart-shared/cart-shared.module';

// components
import { CartDetailsComponent } from './cart-details.component';
import { SaveToWishlistModalComponent } from './save-to-wishlist-modal/save-to-wishlist-modal.component';
import { PromotionFreeDeliveryModule } from '../../promotions/promotion-free-delivery/promotion-free-delivery.module';

@NgModule({
  imports: [
    MediaModule,
    IconModule,
    CartSharedModule,
    CommonModule,
    RouterModule,
    UrlModule,
    IconModule,
    PromotionFreeDeliveryModule,
    ConfigModule.withConfig(<CmsConfig>{
      cmsComponents: {
        CartComponent: {
          component: CartDetailsComponent
        }
      }
    }),
    I18nModule
  ],
  declarations: [CartDetailsComponent, SaveToWishlistModalComponent],
  exports: [CartDetailsComponent],
  entryComponents: [CartDetailsComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CartDetailsModule {}
