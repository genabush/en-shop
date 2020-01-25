import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

// spartacus
import { I18nModule, UrlModule } from '@spartacus/core';
import { MediaModule } from '@spartacus/storefront';
import { IconModule } from '@spartacus/storefront';
import { PromotionsModule } from '@spartacus/storefront';

// vendor
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

// modules
import { ItemCounterModule } from '../shared/custom-quantity-selector/custom-quantity-selector.module';

// components
import { CartItemListComponent } from './cart-item-list/cart-item-list.component';
import { CartItemComponent } from './cart-item/cart-item.component';
import { OrderSummaryComponent } from './order-summary/order-summary.component';
import { CartDeliveryOptionsCardsComponent } from './cart-delivery-options-cards/cart-delivery-options-cards.component';
import { CartLoyaltyPointsComponent } from './cart-loyalty-points/cart-loyalty-points.component';
import { CartItemVariantsComponent } from './cart-item-variants/cart-item-variants.component';
import { PromotionFreeDeliveryModule } from '../promotions/promotion-free-delivery/promotion-free-delivery.module';

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    UrlModule,
    NgbModule,
    PromotionsModule,
    PromotionFreeDeliveryModule,
    I18nModule,
    MediaModule,
    ItemCounterModule,
    IconModule
  ],
  declarations: [
    CartItemComponent,
    CartItemVariantsComponent,
    CartItemListComponent,
    OrderSummaryComponent,
    CartDeliveryOptionsCardsComponent,
    CartLoyaltyPointsComponent
  ],
  exports: [
    CartItemComponent,
    CartItemVariantsComponent,
    CartItemListComponent,
    OrderSummaryComponent,
    CartDeliveryOptionsCardsComponent
  ],
  entryComponents: [OrderSummaryComponent]
})
export class CartSharedModule {}
