import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';

// spartacus
import { IconModule, MediaModule, GenericLinkModule } from '@spartacus/storefront';
import { ConfigModule, I18nModule, UrlModule } from '@spartacus/core';

// vendor
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

// components
import { CustomMiniCartComponent } from './custom-mini-cart-component/custom-mini-cart.component';
import { CustomMiniCartDirective } from 'src/app/directives/custom-mini-cart/custom-mini-cart.directive';
import { CustomMiniCartEntryVariantsComponent } from './custom-mini-cart-entry-variants/custom-mini-cart-entry-variants.component';
import { CustomMiniCartEntryItemComponent } from './custom-mini-cart-entry-item/custom-mini-cart-entry-item.component';
import { PromotionFreeDeliveryModule } from '../promotions/promotion-free-delivery/promotion-free-delivery.module';

@NgModule({
  imports: [
    CommonModule,
    BrowserModule,
    RouterModule,
    MediaModule,
    GenericLinkModule,
    UrlModule,
    NgbModule,
    IconModule,
    I18nModule,
    PromotionFreeDeliveryModule,
    ConfigModule.withConfig({
      cmsComponents: {
        MiniCartComponent: {
          component: CustomMiniCartComponent,
          deps: [CustomMiniCartDirective]
        }
      }
    })
  ],
  declarations: [
    CustomMiniCartComponent,
    CustomMiniCartEntryVariantsComponent,
    CustomMiniCartEntryItemComponent,
    CustomMiniCartDirective
  ],
  exports: [CustomMiniCartComponent, CustomMiniCartEntryVariantsComponent, CustomMiniCartEntryItemComponent],
  entryComponents: [CustomMiniCartComponent, CustomMiniCartEntryVariantsComponent, CustomMiniCartEntryItemComponent]
})
export class CustomMiniCartModule {}
