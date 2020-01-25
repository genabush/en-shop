import { CommonModule } from '@angular/common';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CmsConfig, ConfigModule, I18nModule, UrlModule } from '@spartacus/core';
import { IconModule } from '@spartacus/storefront';
import { SpinnerModule } from '@spartacus/storefront';
import { CartSharedModule } from '../../cart-shared/cart-shared.module';
import { AddToCartComponent } from './add-to-cart.component';
import { AddedToCartDialogComponent } from './added-to-cart-dialog/added-to-cart-dialog.component';
import { BackInStockComponent } from '../../back-in-stock/back-in-stock.component';
import { ReactiveFormsModule } from '@angular/forms';
import { ItemCounterModule } from '../../shared/custom-quantity-selector/custom-quantity-selector.module';
import { BackInStockFormComponent } from '../../back-in-stock/back-in-stock-form/back-in-stock-form.component';
import { MatInputModule } from '@angular/material';
import { WishlistFormModule } from '../../wishlist/wishlist-form/wishlist-form.module';
import { NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { FindInStoreComponent } from '../../find-in-store/find-in-store.component';
import { CollectionPointItemModule } from '../../checkout/fulfillment/collection-point-item/collection-point-item.module';
import { AgmCoreModule } from '@agm/core';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  imports: [
    CartSharedModule,
    CommonModule,
    RouterModule,
    SpinnerModule,
    MatInputModule,
    WishlistFormModule,
    ReactiveFormsModule,
    AgmCoreModule,
    ConfigModule.withConfig(<CmsConfig>{
      cmsComponents: {
        ProductAddToCartComponent: {
          component: AddToCartComponent
        }
      }
    }),
    NgbTooltipModule,
    UrlModule,
    IconModule,
    I18nModule,
    ItemCounterModule,
    CollectionPointItemModule,
    SharedModule
    // AutoFocusDirectiveModule,
  ],
  providers: [],
  declarations: [
    AddToCartComponent,
    AddedToCartDialogComponent,
    BackInStockComponent,
    BackInStockFormComponent,
    FindInStoreComponent
  ],
  entryComponents: [AddToCartComponent, AddedToCartDialogComponent, BackInStockFormComponent],
  exports: [AddToCartComponent, AddedToCartDialogComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AddToCartModule {}
