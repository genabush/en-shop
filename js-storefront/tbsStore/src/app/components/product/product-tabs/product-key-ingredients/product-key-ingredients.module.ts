import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ConfigModule, CmsConfig, I18nModule } from '@spartacus/core';
import { ProductKeyIngredientsTabComponent } from './product-key-ingredients.component';
import { IconModule, GenericLinkModule } from '@spartacus/storefront';

@NgModule({
  imports: [
    CommonModule,
    GenericLinkModule,
    IconModule,
    I18nModule,
    ConfigModule.withConfig(<CmsConfig>{
      cmsComponents: {
        ProductKeyIngredientsTabComponent: {
          component: ProductKeyIngredientsTabComponent
        }
      }
    })
  ],
  declarations: [ProductKeyIngredientsTabComponent],
  entryComponents: [ProductKeyIngredientsTabComponent],
  exports: [ProductKeyIngredientsTabComponent]
})
export class ProductKeyIngredientsTabModule {}
