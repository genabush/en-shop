import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';

import { ProductStoreModule } from './custom-product-store';
import { ProductVariantSelectionStoreModule } from './product-variant-selection';
import { CollectionPointStoreModule } from './collection-point';
import { WishlistsStoreModule } from './wishlists';
import { CisStoreModule } from './collect-in-store';
import { StoreFinderStoreModule } from './store-finder';
import { CartGiftWrapStoreModule } from './cart-gift-wrap';
import { CartPromoVoucherStoreModule } from './cart-promo-voucher';

@NgModule({
  imports: [
    CommonModule,
    ProductVariantSelectionStoreModule,
    CollectionPointStoreModule,
    CisStoreModule,
    WishlistsStoreModule,
    StoreFinderStoreModule,
    CartGiftWrapStoreModule,
    CartPromoVoucherStoreModule,
    StoreModule.forRoot({}),
    EffectsModule.forRoot([]),
    StoreDevtoolsModule.instrument({
      maxAge: 25 // Retains last 25 states
    })
  ],
  declarations: []
})
export class RootStoreModule {}
