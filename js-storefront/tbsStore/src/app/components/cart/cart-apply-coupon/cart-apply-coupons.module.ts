import { CommonModule } from '@angular/common';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

// spartacus
import { CmsConfig, ConfigModule, I18nModule, UrlModule } from '@spartacus/core';
import { PromotionsModule, IconModule } from '@spartacus/storefront';

// modules
import { CartSharedModule } from '../../cart-shared/cart-shared.module';

// components
import { CartApplyCouponComponent } from './cart-apply-coupon.component';
import { CartAppliedCouponsComponent } from './cart-applied-coupons/cart-applied-coupons.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material';

@NgModule({
  imports: [
    ReactiveFormsModule,
    CartSharedModule,
    CommonModule,
    RouterModule,
    UrlModule,
    IconModule,
    PromotionsModule,
    MatInputModule,
    ConfigModule.withConfig(<CmsConfig>{
      cmsComponents: {
        CartApplyCouponComponent: {
          component: CartApplyCouponComponent
        }
      }
    }),
    I18nModule
  ],
  declarations: [CartApplyCouponComponent, CartAppliedCouponsComponent],
  exports: [CartApplyCouponComponent, CartAppliedCouponsComponent],
  entryComponents: [CartApplyCouponComponent, CartAppliedCouponsComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CartApplyCouponsModule {}
