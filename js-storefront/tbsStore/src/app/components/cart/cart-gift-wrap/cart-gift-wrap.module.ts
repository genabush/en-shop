/*
 * Copyright (c)
 * 2020 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

// spartacus
import {
  UrlModule,
  ConfigModule,
  CmsConfig,
  I18nModule,
  GlobalMessageConfig,
  GlobalMessageType
} from '@spartacus/core';

// vendor
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule, MatInputModule } from '@angular/material';
import { IconModule, MediaModule } from '@spartacus/storefront';

// services
import { CartGiftWrapService } from 'src/app/services/cart-gift-wrap-service/cart-gift-wrap.service';

// components
import { CartGiftWrapComponent } from './cart-gift-wrap.component';
import { CartGiftWrapServiceFormComponent } from './gift-wrap-service/cart-gift-wrap-service-form/cart-gift-wrap-service-form.component';
import { CartPersonalMessageFormComponent } from './gift-wrap-message/cart-personal-message-form/cart-personal-message-form.component';
import { CartGiftWrapImageComponent } from './cart-gift-wrap-image/cart-gift-wrap-image.component';
import { CartGiftWrapMessageComponent } from './gift-wrap-message/cart-gift-wrap-message/cart-gift-wrap-message.component';
import { CartGiftWrapServiceComponent } from './gift-wrap-service/cart-gift-wrap-service/cart-gift-wrap-service.component';

function cartGlobalMessageConfigFactory(): GlobalMessageConfig {
  return {
    globalMessages: {
      [GlobalMessageType.MSG_TYPE_INFO]: {
        timeout: 7000
      }
    }
  };
}
@NgModule({
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    CommonModule,
    RouterModule,
    UrlModule,
    IconModule,
    MediaModule,
    ConfigModule.withConfigFactory(cartGlobalMessageConfigFactory),
    ConfigModule.withConfig(<CmsConfig>{
      cmsComponents: {
        GiftWrapBannerCMSComponent: {
          component: CartGiftWrapComponent
        }
      }
    }),
    I18nModule
  ],
  declarations: [
    CartGiftWrapComponent,
    CartGiftWrapServiceFormComponent,
    CartPersonalMessageFormComponent,
    CartGiftWrapImageComponent,
    CartGiftWrapMessageComponent,
    CartGiftWrapServiceComponent
  ],
  exports: [CartGiftWrapComponent, CartGiftWrapServiceFormComponent, CartPersonalMessageFormComponent],
  entryComponents: [CartGiftWrapComponent, CartGiftWrapServiceFormComponent, CartPersonalMessageFormComponent],
  providers: [CartGiftWrapService]
})
export class CartGiftWrapModule {}
