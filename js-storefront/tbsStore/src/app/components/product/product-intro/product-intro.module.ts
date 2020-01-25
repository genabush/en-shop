import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { CmsConfig, ConfigModule, I18nModule } from '@spartacus/core';
import { StarRatingModule } from '@spartacus/storefront';
import { ProductIntroComponent } from './product-intro.component';

@NgModule({
  imports: [
    CommonModule,
    I18nModule,
    StarRatingModule,
    ConfigModule.withConfig(<CmsConfig>{
      cmsComponents: {
        ProductIntroComponent: {
          component: ProductIntroComponent
        }
      }
    })
  ],
  declarations: [ProductIntroComponent],
  entryComponents: [ProductIntroComponent]
})
export class ProductIntroModule {}
