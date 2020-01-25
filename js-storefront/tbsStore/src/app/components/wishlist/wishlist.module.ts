import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';

import { CmsPageGuard, IconModule, MediaModule, SpinnerModule } from '@spartacus/storefront';
import { AuthGuard, I18nModule, NotAuthGuard, UrlModule } from '@spartacus/core';

import { WishlistComponent } from './wishlist.component';
import { WishlistDetailComponent } from './wishlist-detail/wishlist-detail.component';
import { CustomProductListModule } from '../product/product-list/container/custom-product-list.module';
import { WishlistIntroComponent } from './wishlist-intro/wishlist-intro.component';
import { WishlistProductsListingComponent } from './wishlist-products-listing/wishlist-products-listing.component';
import { WishlistListsComponent } from './wishlist-lists/wishlist-lists.component';
import { WishlistProductItemComponent } from './wishlist-product-item/wishlist-product-item.component';
import { AddToCartModule } from '../cart/add-to-cart/add-to-cart.module';
import { WishlistProductsListingRefineComponent } from './wishlist-products-listing-refine/wishlist-products-listing-refine.component';
import { WishlistFormModule } from './wishlist-form/wishlist-form.module';
import { WishlistNameModalComponent } from './wishlist-name-modal/wishlist-name-modal.component';
import { DeleteWishlistModalComponent } from './wishlist-detail/delete-wishlist-modal/delete-wishlist-modal.component';

const staticRoutes: Routes = [
  {
    path: 'my-account/wishlist/:id',
    component: WishlistDetailComponent,
    canActivate: [CmsPageGuard, AuthGuard]
  },
  {
    path: 'my-account/wishlist',
    component: WishlistComponent,
    canActivate: [CmsPageGuard, AuthGuard]
  },
  {
    path: 'wishlist/:id',
    component: WishlistDetailComponent,
    canActivate: [CmsPageGuard, NotAuthGuard]
  },
  {
    path: 'wishlist',
    component: WishlistComponent,
    canActivate: [CmsPageGuard, NotAuthGuard]
  }
];

@NgModule({
  imports: [
    I18nModule,
    IconModule,
    CommonModule,
    MediaModule,
    AddToCartModule,
    CustomProductListModule,
    RouterModule.forChild(staticRoutes),
    WishlistFormModule,
    UrlModule,
    SpinnerModule
  ],
  declarations: [
    WishlistComponent,
    WishlistDetailComponent,
    WishlistIntroComponent,
    WishlistProductsListingComponent,
    WishlistListsComponent,
    WishlistProductItemComponent,
    WishlistProductsListingRefineComponent,
    WishlistNameModalComponent,
    DeleteWishlistModalComponent
  ],
  exports: []
})
export class WishlistModule {}
